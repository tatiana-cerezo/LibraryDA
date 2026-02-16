package org.example.biblioteca.controller;

import org.example.biblioteca.model.Libro;
import org.example.biblioteca.service.ExportService;
import org.example.biblioteca.service.ImportService;
import org.example.biblioteca.service.LibroService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
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
 *  @version 1.2
 */
@Controller
@RequestMapping("/libros")
public class LibroController {

    /** Servicio de negocio para la gestión de libros */
    private final LibroService libroService;
    /** Servicio de negocio para la gestión de exportar libros */
    private final ExportService exportService;
    /** Servicio de negocio para la gestión de importar libros */
    private final ImportService importService;

    /**
     * Constructor con inyección de dependencias.
     *
     * @param libroService servicio de libros
     * @param exportService servicio de exportación
     * @param importService servicio de importación
     */
    public LibroController(LibroService libroService,  ExportService exportService, ImportService importService) {
        this.libroService = libroService;
        this.exportService = exportService;
        this.importService = importService;
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
    public String guardar(@ModelAttribute Libro libro, RedirectAttributes redirectAttributes) {
        if (libro.getAnioPublicacion() != null &&
                (libro.getAnioPublicacion() < 1900 || libro.getAnioPublicacion() > 2026)) {
            redirectAttributes.addFlashAttribute("error", "libros.anio.invalido");
            return "redirect:/libros/nuevo";
        }

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
            redirectAttributes.addFlashAttribute("mensaje", "libro.mensaje.eliminado");
        } else {
            redirectAttributes.addFlashAttribute("error", "libro.mensaje.no.eliminado");
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
        if (titulo == null || titulo.trim().isEmpty()) {
            return "redirect:/libros";
        }
        model.addAttribute("libros", libroService.findByTitulo(titulo));
        return "libros/listar";
    }

    /**
     * Exporta todos los libros a un archivo JSON descargable.
     *
     * @return archivo JSON con todos los libros
     */
    @GetMapping("/exportar")
    public ResponseEntity<byte[]> exportar() {
        try {
            String json = exportService.exportarLibros();
            byte[] contenido = json.getBytes(StandardCharsets.UTF_8);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setContentDispositionFormData("attachment", "libros.json");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(contenido);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Importa libros desde un archivo JSON.
     * El archivo debe cumplir con el schema definido en libros-schema.json.
     *
     * @param archivo archivo JSON con los libros a importar
     * @param redirectAttributes atributos para mensajes flash
     * @return redirección a la lista de libros
     */
    @PostMapping("/importar")
    public String importar(@RequestParam("archivo") MultipartFile archivo, RedirectAttributes redirectAttributes) {
        try {
            String json = new String(archivo.getBytes(), StandardCharsets.UTF_8);
            int cantidad = importService.importarLibros(json);
            redirectAttributes.addFlashAttribute("mensaj", "libros.importados");
            redirectAttributes.addFlashAttribute("cantidad", cantidad);
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", "libros.import.error.validacion");
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "libros.import.error.lectura");
        }
        return "redirect:/libros";
    }
}
