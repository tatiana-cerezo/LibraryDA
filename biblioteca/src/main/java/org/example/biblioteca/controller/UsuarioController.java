package org.example.biblioteca.controller;

import org.example.biblioteca.model.Rol;
import org.example.biblioteca.model.Usuario;
import org.example.biblioteca.service.UsuarioService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * Controlador MVC para la gestión de usuarios.
 * <p>
 * Maneja las peticiones web relacionadas con el listado, creación,
 * edición y eliminación de usuarios.
 *
 *  @author Tatiana Cerezo
 *  @version 1.0
 */
@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    /** Servicio de negocio para la gestión de usuarios */
    private final UsuarioService usuarioService;
    /** Codificador de contraseñas para el almacenamiento seguro */
    private final PasswordEncoder passwordEncoder;

    /**
     * Constructor con inyección de dependencias.
     *
     * @param usuarioService servicio de usuarios
     * @param passwordEncoder codificador de contraseñas
     */
    public UsuarioController(UsuarioService usuarioService, PasswordEncoder passwordEncoder) {
        this.usuarioService = usuarioService;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Muestra el listado de usuarios.
     *
     * @param model modelo para la vista
     * @return vista de listado de usuarios
     */
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("usuarios", usuarioService.findAll());
        return "usuarios/listar";
    }

    /**
     * Muestra el formulario para crear un nuevo usuario.
     *
     * @param model modelo para la vista
     * @return vista del formulario de usuario
     */
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("usuario", new Usuario());
        model.addAttribute("roles", Rol.values());
        return "usuarios/formulario";
    }

    /**
     * Guarda un usuario nuevo o actualizado.
     * <p>
     * La contraseña se codifica antes de persistir el usuario.
     *
     * @param usuario usuario recibido del formulario
     * @return redirección al listado de usuarios
     */
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Usuario usuario) {
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        usuarioService.save(usuario);
        return "redirect:/usuarios";
    }

    /**
     * Muestra el formulario para editar un usuario existente.
     *
     * @param id identificador del usuario
     * @param model modelo para la vista
     * @return vista del formulario o redirección si no existe
     */
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        Optional<Usuario> usuario = usuarioService.findById(id);
        if (usuario.isPresent()) {
            model.addAttribute("usuario", usuario.get());
            model.addAttribute("roles", Rol.values());
            return "usuarios/formulario";
        }
        return "redirect:/usuarios";
    }

    /**
     * Elimina un usuario por su identificador.
     *
     * @param id identificador del usuario
     * @return redirección al listado de usuarios
     */
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        usuarioService.deleteById(id);
        return "redirect:/usuarios";
    }
}