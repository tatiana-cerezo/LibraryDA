package org.example.biblioteca.service;

import org.example.biblioteca.model.EstadoPrestamo;
import org.example.biblioteca.model.Libro;
import org.example.biblioteca.repository.LibroRepository;
import org.example.biblioteca.repository.PrestamoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LibroService {

    private final LibroRepository libroRepository;
    private final PrestamoRepository prestamoRepository;

    public LibroService(LibroRepository libroRepository, PrestamoRepository prestamoRepository) {
        this.libroRepository = libroRepository;
        this.prestamoRepository = prestamoRepository;
    }

    public List<Libro> findAll() {
        return libroRepository.findAll();
    }

    public Optional<Libro> findById(Long id) {
        return libroRepository.findById(id);
    }

    public List<Libro> findByTitulo(String titulo) {
        return libroRepository.findByTituloContainingIgnoreCase(titulo);
    }

    public List<Libro> findByAutor(String autor) {
        return libroRepository.findByAutorContainingIgnoreCase(autor);
    }

    public List<Libro> findByCategoria(String categoria) {
        return libroRepository.findByCategoria(categoria);
    }

    public Libro save(Libro libro) {
        return libroRepository.save(libro);
    }

    public void deleteById(Long id) {
        libroRepository.deleteById(id);
    }

    public int getDisponibles(Long libroId) {
        Optional<Libro> libro = libroRepository.findById(libroId);
        if (libro.isPresent()) {
            int prestamosActivos = prestamoRepository.findByLibroIdAndEstado(libroId, EstadoPrestamo.ACTIVO).size();
            return libro.get().getCopias() - prestamosActivos;
        }
        return 0;
    }
}
