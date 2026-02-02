package org.example.biblioteca.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.biblioteca.model.Rol;
import org.example.biblioteca.model.Usuario;
import org.example.biblioteca.repository.PrestamoRepository;
import org.example.biblioteca.service.UsuarioService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Controlador MVC para la gestión de usuarios.
 * <p>
 * Maneja las peticiones web relacionadas con el listado, creación,
 * edición y eliminación de usuarios.
 *
 *  @author Tatiana Cerezo
 *  @version 1.2
 */
@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    /** Servicio de negocio para la gestión de usuarios */
    private final UsuarioService usuarioService;
    /** Codificador de contraseñas para el almacenamiento seguro */
    private final PasswordEncoder passwordEncoder;
    /** Repositorio para el acceso y persistencia de préstamos */
    private final PrestamoRepository prestamoRepository;

    /**
     * Constructor con inyección de dependencias.
     *
     * @param usuarioService servicio de usuarios
     * @param passwordEncoder codificador de contraseñas
     * @param prestamoRepository repositorio de préstamos
     */
    public UsuarioController(UsuarioService usuarioService, PasswordEncoder passwordEncoder, PrestamoRepository prestamoRepository) {
        this.usuarioService = usuarioService;
        this.passwordEncoder = passwordEncoder;
        this.prestamoRepository = prestamoRepository;
    }

    /**
     * Muestra el listado de usuarios y
     * mapa que recoge cuáles pueden eliminarse y cuáles no.
     *
     * @param model modelo para la vista
     * @param userDetails usuario autenticado en la sesión (puede ser null)
     * @return vista de listado de usuarios
     */
    @GetMapping
    public String listar(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        Optional<Usuario> usuarioActual = usuarioService.findByEmail(userDetails.getUsername());

        if (usuarioActual.isPresent()) {
            boolean esAdmin = usuarioActual.get().getRol().name().equals("ADMIN");
            model.addAttribute("esAdmin", esAdmin);

            List<Usuario> usuarios;
            if (esAdmin) {
                usuarios = usuarioService.findAll();
            } else {
                usuarios = List.of(usuarioActual.get());
            }
            model.addAttribute("usuarios", usuarios);

            Map<Long, Boolean> puedeEliminarse = new HashMap<>();
            for (Usuario usuario : usuarios) {
                puedeEliminarse.put(usuario.getId(), usuarioService.puedeEliminarse(usuario.getId()));
            }
            model.addAttribute("puedeEliminarse", puedeEliminarse);
        }

        return "usuarios/listar";
    }

    /**
     * Muestra el formulario para crear un nuevo usuario.
     *
     * @return vista del formulario de registro
     */
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo() {
        return "reidrect:/registro";
    }

    /**
     * Guarda un usuario editado.
     * Solo si el usuario autenticado es ADMIN puede editar el rol.
     * <p>
     * La contraseña se codifica antes de persistir el usuario.
     *
     * @param usuario usuario recibido del formulario
     * @param userDetails usuario autenticado en la sesión (puede ser null)
     * @param redirectAttributes atributos para mensajes flash
     * @return redirección al listado de usuarios
     */
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Usuario usuario,
                          @AuthenticationPrincipal UserDetails userDetails,
                          RedirectAttributes redirectAttributes) {
        Optional<Usuario> usuarioActual = usuarioService.findByEmail(userDetails.getUsername());
        boolean esAdmin = usuarioActual.isPresent() && usuarioActual.get().getRol().name().equals("ADMIN");

        if (usuario.getId() != null) {
            Optional<Usuario> usuarioExistente = usuarioService.findById(usuario.getId());
            if (usuarioExistente.isPresent()) {
                Usuario u = usuarioExistente.get();
                u.setNombre(usuario.getNombre());
                u.setEmail(usuario.getEmail());

                if (esAdmin) {
                    u.setRol(usuario.getRol());
                }

                if (usuario.getPassword() != null && !usuario.getPassword().isBlank()) {
                    u.setPassword(passwordEncoder.encode(usuario.getPassword()));
                }

                usuarioService.save(u);
                redirectAttributes.addFlashAttribute("mensaje", "usuario.mensaje.actualizado");
            }
        }

        return "redirect:/usuarios";
    }

    /**
     * Muestra el formulario para editar un usuario existente con sus datos cargados.
     *
     * @param id identificador del usuario
     * @param model modelo para la vista
     * @param userDetails usuario autenticado en la sesión (puede ser null)
     * @return vista del formulario o redirección si no existe
     */
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model,
                                          @AuthenticationPrincipal UserDetails userDetails) {
        Optional<Usuario> usuarioActual = usuarioService.findByEmail(userDetails.getUsername());
        Optional<Usuario> usuario = usuarioService.findById(id);

        if (usuario.isPresent() && usuarioActual.isPresent()) {
            boolean esAdmin = usuarioActual.get().getRol().name().equals("ADMIN");

            if (!esAdmin && !usuarioActual.get().getId().equals(id)) {
                return "redirect:/usuarios";
            }

            model.addAttribute("usuario", usuario.get());
            model.addAttribute("roles", Rol.values());
            model.addAttribute("esAdmin", esAdmin);
            return "usuarios/formulario";
        }

        return "redirect:/usuarios";
    }

    /**
     * Elimina un usuario por su id si es posible.
     * Envía mensaje con el resultado.
     * Si el usuario eliminado es el mismo que está autenticado,
     * se fuerza el cierre de sesión.
     *
     * @param id identificador del usuario
     * @param userDetails usuario autenticado en la sesión (puede ser null)
     * @param request petición HTTP actual
     * @param response respuesta HTTP actual
     * @param redirectAttributes atributos para mensajes flash
     * @return redirección al listado de usuarios
     */
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id,
                           @AuthenticationPrincipal UserDetails userDetails,
                           HttpServletRequest request,
                           HttpServletResponse response,
                           RedirectAttributes redirectAttributes) {
        Optional<Usuario> usuarioActual = usuarioService.findByEmail(userDetails.getUsername());

        if (usuarioActual.isPresent()) {
            boolean esAdmin = usuarioActual.get().getRol().name().equals("ADMIN");
            boolean esPropio = usuarioActual.get().getId().equals(id);

            if (!esAdmin && !esPropio) {
                return "redirect:/usuarios";
            }

            if (usuarioService.eliminarConPrestamos(id, prestamoRepository)) {
                redirectAttributes.addFlashAttribute("mensaje", "usuario.mensaje.eliminado");
                if (esPropio) {
                    new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
                    return "redirect:/login?eliminado";
                }
            } else {
                redirectAttributes.addFlashAttribute("error", "usuario.mensaje.no.eliminado");
            }
        }

        return "redirect:/usuarios";
    }
}