package org.example.biblioteca.service;

import org.example.biblioteca.model.EstadoPrestamo;
import org.example.biblioteca.model.Libro;
import org.example.biblioteca.model.Prestamo;
import org.example.biblioteca.repository.LibroRepository;
import org.example.biblioteca.repository.PrestamoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Servicio de negocio para la gestión de libros.
 * <p>
 * Proporciona operaciones CRUD y lógica relacionada con la disponibilidad
 * de libros en función de los préstamos activos.
 *
 *  @author Tatiana Cerezo
 *  @version 1.1
 */
@Service
public class LibroService {

    /** Repositorio para operaciones CRUD sobre libros */
    private final LibroRepository libroRepository;
    /** Repositorio para consultar préstamos asociados a libros */
    private final PrestamoRepository prestamoRepository;

    /**
     * Constructor con inyección de dependencias.
     *
     * @param libroRepository repositorio de libros
     * @param prestamoRepository repositorio de préstamos
     */
    public LibroService(LibroRepository libroRepository, PrestamoRepository prestamoRepository) {
        this.libroRepository = libroRepository;
        this.prestamoRepository = prestamoRepository;
    }

    /**
     * Obtiene todos los libros del sistema.
     *
     * @return lista de libros
     */
    public List<Libro> findAll() {
        return libroRepository.findAll();
    }

    /**
     * Busca un libro por su identificador.
     *
     * @param id identificador del libro
     * @return libro encontrado o vacío si no existe
     */
    public Optional<Libro> findById(Long id) {
        return libroRepository.findById(id);
    }

    /**
     * Busca libros cuyo título contenga el texto indicado, ignorando mayúsculas.
     *
     * @param titulo texto a buscar en el título
     * @return lista de libros coincidentes
     */
    public List<Libro> findByTitulo(String titulo) {
        return libroRepository.findByTituloContainingIgnoreCase(titulo);
    }

    /**
     * Busca libros por autor, ignorando mayúsculas.
     *
     * @param autor nombre del autor
     * @return lista de libros del autor
     */
    public List<Libro> findByAutor(String autor) {
        return libroRepository.findByAutorContainingIgnoreCase(autor);
    }

    /**
     * Busca libros por categoría.
     *
     * @param categoria categoría del libro
     * @return lista de libros de la categoría indicada
     */
    public List<Libro> findByCategoria(String categoria) {
        return libroRepository.findByCategoria(categoria);
    }

    /**
     * Guarda o actualiza un libro.
     *
     * @param libro libro a persistir
     * @return libro guardado
     */
    public Libro save(Libro libro) {
        return libroRepository.save(libro);
    }

    /**
     * Elimina un libro por su identificador.
     *
     * @param id identificador del libro
     */
    public void deleteById(Long id) {
        libroRepository.deleteById(id);
    }

    /**
     * Calcula el número de copias disponibles de un libro.
     * <p>
     * Se restan los préstamos activos al total de copias.
     *
     * @param libroId identificador del libro
     * @return número de copias disponibles
     */
    public int getDisponibles(Long libroId) {
        Optional<Libro> libro = libroRepository.findById(libroId);
        if (libro.isPresent()) {
            int prestamosActivos = prestamoRepository.findByLibroIdAndEstado(libroId, EstadoPrestamo.ACTIVO).size();
            return libro.get().getCopias() - prestamosActivos;
        }
        return 0;
    }

    /**
     * Obtiene los libros que tienen al menos una copia disponible.
     *
     * @return lista de libros disponibles
     */
    public List<Libro> findDisponibles() {
        List<Libro> todos = libroRepository.findAll();
        return todos.stream()
                .filter(libro -> getDisponibles(libro.getId()) > 0)
                .toList();
    }

    /**
     * Verifica si un libro puede ser eliminado.
     * Solo puede eliminarse si no tiene préstamos activos o vencidos.
     *
     * @param libroId ID del libro
     * @return true si puede eliminarse
     */
    public boolean puedeEliminarse(Long libroId) {
        List<Prestamo> prestamosActivos = prestamoRepository.findByLibroIdAndEstado(libroId, EstadoPrestamo.ACTIVO);
        List<Prestamo> prestamosVencidos = prestamoRepository.findByLibroIdAndEstado(libroId, EstadoPrestamo.VENCIDO);
        return prestamosActivos.isEmpty() && prestamosVencidos.isEmpty();
    }

    /**
     * Elimina un libro y sus préstamos devueltos asociados.
     *
     * @param id ID del libro a eliminar
     * @return true si se eliminó correctamente
     */
    public boolean eliminarConPrestamos(Long id) {
        if (!puedeEliminarse(id)) {
            return false;
        }

        List<Prestamo> prestamosDevueltos = prestamoRepository.findByLibroIdAndEstado(id, EstadoPrestamo.DEVUELTO);
        for (Prestamo p : prestamosDevueltos) {
            prestamoRepository.deleteById(p.getId());
        }

        libroRepository.deleteById(id);
        return true;
    }
}
