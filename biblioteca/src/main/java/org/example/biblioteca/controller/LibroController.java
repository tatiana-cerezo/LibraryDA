package org.example.biblioteca.controller;

import org.example.biblioteca.model.Libro;
import org.example.biblioteca.service.LibroService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * Controlador MVC para la gestión de libros.
 * <p>
 * Maneja las peticiones web relacionadas con el listado, creación,
 * edición, eliminación y búsqueda de libros.
 *
 *  @author Tatiana Cerezo
 *  @version 1.0
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
     * Muestra el listado de libros.
     *
     * @param model modelo para la vista
     * @return vista de listado de libros
     */
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("libros", libroService.findAll());
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
     * Elimina un libro por su identificador.
     *
     * @param id identificador del libro
     * @return redirección al listado de libros
     */
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        libroService.deleteById(id);
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
