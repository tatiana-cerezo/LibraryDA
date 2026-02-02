package org.example.biblioteca.controller;

import org.example.biblioteca.model.Libro;
import org.example.biblioteca.service.LibroService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Controlador MVC para la gestión de libros.
 * <p>
 * Maneja las peticiones web relacionadas con el listado, creación,
 * edición, eliminación y búsqueda de libros.
 *
 *  @author Tatiana Cerezo
 *  @version 1.1
 */
@Controller
@RequestMapping("/libros")
public class LibroController {

    /** Servicio de negocio para la gestión de libros */
    private final LibroService libroService;

    /**
     * Constructor con inyección de dependencias.
     *
     * @param libroService servicio de libros
     */
    public LibroController(LibroService libroService) {
        this.libroService = libroService;
    }

    /**
     * Muestra el listado de libros y
     * mapa que recoge cuáles pueden eliminarse y cuáles no.
     *
     * @param model modelo para la vista
     * @return vista de listado de libros
     */
    @GetMapping
    public String listar(Model model) {
        List<Libro> libros = libroService.findAll();
        model.addAttribute("libros", libros);

        Map<Long, Boolean> puedeEliminarse = new HashMap<>();
        for (Libro libro : libros) {
            puedeEliminarse.put(libro.getId(), libroService.puedeEliminarse(libro.getId()));
        }
        model.addAttribute("puedeEliminarse", puedeEliminarse);
        return "libros/listar";
    }

    /**
     * Muestra el formulario para crear un nuevo libro.
     *
     * @param model modelo para la vista
     * @return vista del formulario de libro
     */
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("libro", new Libro());
        return "libros/formulario";
    }

    /**
     * Guarda un libro nuevo o actualizado.
     *
     * @param libro libro recibido del formulario
     * @return redirección al listado de libros
     */
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Libro libro) {
        libroService.save(libro);
        return "redirect:/libros";
    }

    /**
     * Muestra el formulario para editar un libro existente.
     *
     * @param id identificador del libro
     * @param model modelo para la vista
     * @return vista del formulario o redirección si no existe
     */
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        Optional<Libro> libro = libroService.findById(id);
        if (libro.isPresent()) {
            model.addAttribute("libro", libro.get());
            return "libros/formulario";
        }
        return "redirect:/libros";
    }

    /**
     * Elimina un libro por su identificador si es posible.
     * Envía mensaje con el resultado.
     *
     * @param id identificador del libro
     * @param redirectAttributes atributos para mensajes flash
     * @return redirección al listado de libros
     */
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        if (libroService.eliminarConPrestamos(id)) {
            redirectAttributes.addFlashAttribute("mensaje", "Libro eliminado correctamente");
        } else {
            redirectAttributes.addFlashAttribute("error", "No se puede eliminar el libro porque tiene préstamos activos o vencidos");
        }
        return "redirect:/libros";
    }

    /**
     * Busca libros por título.
     *
     * @param titulo texto a buscar en el título
     * @param model modelo para la vista
     * @return vista de listado con los resultados de búsqueda
     */
    @GetMapping("/buscar")
    public String buscar(@RequestParam String titulo, Model model) {
        model.addAttribute("libros", libroService.findByTitulo(titulo));
        return "libros/listar";
    }
}
