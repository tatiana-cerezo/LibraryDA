package org.example.biblioteca.service;

import org.example.biblioteca.model.EstadoPrestamo;
import org.example.biblioteca.model.Libro;
import org.example.biblioteca.model.Prestamo;
import org.example.biblioteca.model.Usuario;
import org.example.biblioteca.repository.PrestamoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PrestamoService {

    private final PrestamoRepository prestamoRepository;

    public PrestamoService(PrestamoRepository prestamoRepository) {
        this.prestamoRepository = prestamoRepository;
    }

    public List<Prestamo> findAll() {
        List<Prestamo> prestamos = prestamoRepository.findAll();
        actualizarVencidos(prestamos);
        return prestamos;
    }

    public Optional<Prestamo> findById(Long id) {
        Optional<Prestamo> prestamo = prestamoRepository.findById(id);
        prestamo.ifPresent(p -> actualizarVencido(p));
        return prestamo;
    }

    public List<Prestamo> findByUsuarioId(Long usuarioId) {
        List<Prestamo> prestamos = prestamoRepository.findByUsuarioId(usuarioId);
        actualizarVencidos(prestamos);
        return prestamos;
    }

    public List<Prestamo> findByLibroId(Long libroId) {
        List<Prestamo> prestamos = prestamoRepository.findByLibroId(libroId);
        actualizarVencidos(prestamos);
        return prestamos;
    }

    public List<Prestamo> findByEstado(EstadoPrestamo estado) {
        List<Prestamo> prestamos = prestamoRepository.findByEstado(estado);
        actualizarVencidos(prestamos);
        return prestamos;
    }

    public Prestamo crear(Libro libro, Usuario usuario, LocalDate fechaDevolucion) {
        Prestamo prestamo = new Prestamo();
        prestamo.setLibro(libro);
        prestamo.setUsuario(usuario);
        prestamo.setFechaPrestamo(LocalDate.now());
        prestamo.setFechaDevolucion(fechaDevolucion);
        prestamo.setEstado(EstadoPrestamo.ACTIVO);
        return prestamoRepository.save(prestamo);
    }

    public Prestamo save(Prestamo prestamo) {
        return prestamoRepository.save(prestamo);
    }

    public void deleteById(Long id) {
        prestamoRepository.deleteById(id);
    }

    public Prestamo devolver(Long id) {
        Optional<Prestamo> prestamo = prestamoRepository.findById(id);
        if (prestamo.isPresent()) {
            Prestamo p = prestamo.get();
            p.setEstado(EstadoPrestamo.DEVUELTO);
            return prestamoRepository.save(p);
        }
        return null;
    }

    private void actualizarVencidos(List<Prestamo> prestamos) {
        for (Prestamo p : prestamos) {
            actualizarVencido(p);
        }
    }

    private void actualizarVencido(Prestamo prestamo) {
        if (prestamo.getEstado() == EstadoPrestamo.ACTIVO &&
                prestamo.getFechaDevolucion().isBefore(LocalDate.now())) {
            prestamo.setEstado(EstadoPrestamo.VENCIDO);
            prestamoRepository.save(prestamo);
        }
    }
}
