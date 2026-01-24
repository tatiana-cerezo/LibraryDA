package org.example.biblioteca.controller;

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
import java.util.Optional;

@Controller
@RequestMapping("/prestamos")
public class PrestamoController {

    private final PrestamoService prestamoService;
    private final LibroService libroService;
    private final UsuarioService usuarioService;

    public PrestamoController(PrestamoService prestamoService, LibroService libroService, UsuarioService usuarioService) {
        this.prestamoService = prestamoService;
        this.libroService = libroService;
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public String listar(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        Optional<Usuario> usuario = usuarioService.findByEmail(userDetails.getUsername());

        if (usuario.isPresent() && usuario.get().getRol().name().equals("ADMIN")) {
            model.addAttribute("prestamos", prestamoService.findAll());
        } else if (usuario.isPresent()) {
            model.addAttribute("prestamos", prestamoService.findByUsuarioId(usuario.get().getId()));
        }

        return "prestamos/listar";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        Optional<Usuario> usuario = usuarioService.findByEmail(userDetails.getUsername());

        model.addAttribute("libros", libroService.findDisponibles());

        if (usuario.isPresent() && usuario.get().getRol().name().equals("ADMIN")) {
            model.addAttribute("usuarios", usuarioService.findAll());
            model.addAttribute("esAdmin", true);
        } else {
            model.addAttribute("usuarioActual", usuario.orElse(null));
            model.addAttribute("esAdmin", false);
        }

        return "prestamos/formulario";
    }

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

    @GetMapping("/devolver/{id}")
    public String devolver(@PathVariable Long id) {
        prestamoService.devolver(id);
        return "redirect:/prestamos";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        prestamoService.deleteById(id);
        return "redirect:/prestamos";
    }
}
