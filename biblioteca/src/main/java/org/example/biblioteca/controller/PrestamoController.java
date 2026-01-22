package org.example.biblioteca.controller;

import org.example.biblioteca.model.Prestamo;
import org.example.biblioteca.service.LibroService;
import org.example.biblioteca.service.PrestamoService;
import org.example.biblioteca.service.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    public String listar(Model model) {
        model.addAttribute("prestamos", prestamoService.findAll());
        return "prestamos/listar";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("libros", libroService.findAll());
        model.addAttribute("usuarios", usuarioService.findAll());
        return "prestamos/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(@RequestParam Long libroId, @RequestParam Long usuarioId, @RequestParam int diasPrestamo) {
        var libro = libroService.findById(libroId);
        var usuario = usuarioService.findById(usuarioId);

        if (libro.isPresent() && usuario.isPresent()) {
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
