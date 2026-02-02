package org.example.biblioteca.controller;

import org.example.biblioteca.model.EstadoPrestamo;
import org.example.biblioteca.model.Libro;
import org.example.biblioteca.model.Prestamo;
import org.example.biblioteca.model.Usuario;
import org.example.biblioteca.service.LibroService;
import org.example.biblioteca.service.PrestamoService;
import org.example.biblioteca.service.UsuarioService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Controlador MVC para la gestión de préstamos.
 * <p>
 * Maneja las peticiones web relacionadas con el listado, creación,
 * edición y eliminación de préstamos.
 *
 *  @author Tatiana Cerezo
 *  @version 1.3
 */
@Controller
@RequestMapping("/prestamos")
public class PrestamoController {

    /** Servicio de negocio para la gestión de préstamos */
    private final PrestamoService prestamoService;
    /** Servicio de negocio para la gestión de libros */
    private final LibroService libroService;
    /** Servicio de negocio para la gestión de usuarios */
    private final UsuarioService usuarioService;

    /**
     * Constructor con inyección de dependencias.
     *
     * @param prestamoService servicio de préstamos
     * @param libroService servicio de libros
     * @param usuarioService servicio de usuarios
     */
    public PrestamoController(PrestamoService prestamoService, LibroService libroService, UsuarioService usuarioService) {
        this.prestamoService = prestamoService;
        this.libroService = libroService;
        this.usuarioService = usuarioService;
    }

    /**
     * Muestra el listado de préstamos.
     * <p>
     * Si el usuario es ADMIN, muestra todos los préstamos
     * Si el usuario es USER, muestra solo sus propios préstamos
     *
     * @param model modelo para la vista
     * @param userDetails usuario autenticado
     * @return vista de listado de préstamos
     */
    @GetMapping
    public String listar(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        Optional<Usuario> usuario = usuarioService.findByEmail(userDetails.getUsername());

        if (usuario.isPresent()) {
            Long usuarioId = usuario.get().getRol().name().equals("ADMIN") ? null : usuario.get().getId();
            model.addAttribute("prestamosActivos", prestamoService.findActivosYVencidosOrdenados(usuarioId));
            model.addAttribute("prestamosDevueltos", prestamoService.findDevueltosOrdenados(usuarioId));
        }

        return "prestamos/listar";
    }

    /**
     * Muestra el formulario para crear un nuevo préstamo.
     * <p>
     * Carga libros disponibles y, si el usuario es ADMIN, permite seleccionar
     * cualquier usuario; si no, asigna automáticamente al usuario autenticado.
     *
     * @param model modelo para la vista
     * @param userDetails usuario autenticado
     * @return vista del formulario de préstamo
     */
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model,
                                         @AuthenticationPrincipal UserDetails userDetails,
                                         @RequestParam(required = false) String titulo,
                                         @RequestParam(required = false) Long libroId) {
        Optional<Usuario> usuario = usuarioService.findByEmail(userDetails.getUsername());

        List<Libro> libros = List.of();
        if (libroId == null) {
            libros = (titulo != null && !titulo.isBlank())
                    ? libroService.findByTitulo(titulo).stream()
                    .filter(l -> libroService.getDisponibles(l.getId()) > 0)
                    .toList()
                    : List.of();
        } else {
            libroService.findById(libroId).ifPresent(l -> model.addAttribute("libroSeleccionado", l));
        }

        model.addAttribute("busquedaRealizada", titulo != null && !titulo.isBlank());

        model.addAttribute("hayLibrosDisponibles", !libroService.findDisponibles().isEmpty());

        model.addAttribute("libros", libros);
        model.addAttribute("tituloBuscado", titulo);

        if (usuario.isPresent() && usuario.get().getRol().name().equals("ADMIN")) {
            model.addAttribute("usuarios", usuarioService.findAll());
            model.addAttribute("esAdmin", true);
        } else {
            model.addAttribute("usuarioActual", usuario.orElse(null));
            model.addAttribute("esAdmin", false);
        }

        return "prestamos/formulario";
    }

    /**
     * Guarda un nuevo préstamo.
     * <p>
     * Se calcula la fecha de devolución sumando los días indicados.
     * Si el usuario es ADMIN, puede seleccionar otro usuario; si no,
     * se asigna automáticamente al usuario autenticado.
     * Verifica que haya copias disponibles antes de crear el préstamo.
     *
     * @param libroId identificador del libro a prestar
     * @param usuarioId identificador del usuario (opcional, solo ADMIN)
     * @param diasPrestamo número de días del préstamo
     * @param userDetails usuario autenticado
     * @param redirectAttributes atributos para mensajes flash
     * @return redirección al listado de préstamos o al formulario si no hay copias
     */
    @PostMapping("/guardar")
    public String guardar(@RequestParam Long libroId,
                          @RequestParam(required = false) Long usuarioId,
                          @RequestParam int diasPrestamo,
                          @AuthenticationPrincipal UserDetails userDetails,
                          RedirectAttributes redirectAttributes) {

        var libro = libroService.findById(libroId);

        Optional<Usuario> usuario;
        if (usuarioId != null) {
            usuario = usuarioService.findById(usuarioId);
        } else {
            usuario = usuarioService.findByEmail(userDetails.getUsername());
        }

        if (libro.isPresent() && usuario.isPresent()) {
            if (!prestamoService.hayCopiasDisponibles(libro.get())) {
                redirectAttributes.addFlashAttribute("error", "No hay copias disponibles de este libro");
                return "redirect:/prestamos/nuevo";
            }

            LocalDate fechaDevolucion = LocalDate.now().plusDays(diasPrestamo);
            prestamoService.crear(libro.get(), usuario.get(), fechaDevolucion);
        }
        return "redirect:/prestamos";
    }

    /**
     * Devuelve un préstamo por su identificador.
     *
     * @param id identificador del préstamo
     * @return redirección al listado de préstamos
     */
    @GetMapping("/devolver/{id}")
    public String devolver(@PathVariable Long id) {
        prestamoService.devolver(id);
        return "redirect:/prestamos";
    }

    /**
     * Elimina un préstamo por su identificador solo si está DEVUELTO.
     * Envía mensaje mostrando resultado.
     *
     * @param id identificador del préstamo
     * @return redirección al listado de préstamos
     */
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Optional<Prestamo> prestamo = prestamoService.findById(id);
        if (prestamo.isPresent() && prestamo.get().getEstado() == EstadoPrestamo.DEVUELTO) {
            prestamoService.deleteById(id);
            redirectAttributes.addFlashAttribute("mensaje", "prestamo.mensaje.eliminado");
        } else {
            redirectAttributes.addFlashAttribute("error", "prestamo.mensaje.no.eliminado");
        }

        return "redirect:/prestamos";
    }
}
