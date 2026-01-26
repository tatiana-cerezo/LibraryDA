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

/**
 * Servicio de negocio para la gestión de préstamos.
 * <p>
 * Encapsula la lógica relacionada con la creación, consulta y devolución
 * de préstamos, así como la actualización automática de préstamos vencidos.
 *
 * @author Tatiana Cerezo
 * @version 1.0
 */
@Service
public class PrestamoService {

    /** Repositorio para el acceso y persistencia de préstamos */
    private final PrestamoRepository prestamoRepository;

    /**
     * Constructor con inyección de dependencias.
     *
     * @param prestamoRepository repositorio de préstamos
     */
    public PrestamoService(PrestamoRepository prestamoRepository) {
        this.prestamoRepository = prestamoRepository;
    }

    /**
     * Obtiene todos los préstamos del sistema y actualiza los vencidos.
     *
     * @return lista de préstamos
     */
    public List<Prestamo> findAll() {
        List<Prestamo> prestamos = prestamoRepository.findAll();
        actualizarVencidos(prestamos);
        return prestamos;
    }

    /**
     * Busca un préstamo por su identificador y actualiza su estado si está vencido.
     *
     * @param id identificador del préstamo
     * @return préstamo encontrado o vacío si no existe
     */
    public Optional<Prestamo> findById(Long id) {
        Optional<Prestamo> prestamo = prestamoRepository.findById(id);
        prestamo.ifPresent(p -> actualizarVencido(p));
        return prestamo;
    }

    /**
     * Obtiene los préstamos asociados a un usuario.
     *
     * @param usuarioId identificador del usuario
     * @return lista de préstamos del usuario
     */
    public List<Prestamo> findByUsuarioId(Long usuarioId) {
        List<Prestamo> prestamos = prestamoRepository.findByUsuarioId(usuarioId);
        actualizarVencidos(prestamos);
        return prestamos;
    }

    /**
     * Obtiene los préstamos asociados a un libro.
     *
     * @param libroId identificador del libro
     * @return lista de préstamos del libro
     */
    public List<Prestamo> findByLibroId(Long libroId) {
        List<Prestamo> prestamos = prestamoRepository.findByLibroId(libroId);
        actualizarVencidos(prestamos);
        return prestamos;
    }

    /**
     * Obtiene los préstamos según su estado.
     *
     * @param estado estado del préstamo
     * @return lista de préstamos con el estado indicado
     */
    public List<Prestamo> findByEstado(EstadoPrestamo estado) {
        List<Prestamo> prestamos = prestamoRepository.findByEstado(estado);
        actualizarVencidos(prestamos);
        return prestamos;
    }

    /**
     * Cuenta los préstamos activos de un libro.
     *
     * @param libroId identificador del libro
     * @return número de préstamos activos
     */
    public int contarPrestamosActivos(Long libroId) {
        return prestamoRepository.findByLibroIdAndEstado(libroId, EstadoPrestamo.ACTIVO).size();
    }

    /**
     * Comprueba si existen copias disponibles de un libro.
     *
     * @param libro libro a comprobar
     * @return {@code true} si hay copias disponibles, {@code false} en caso contrario
     */
    public boolean hayCopiasDisponibles(Libro libro) {
        int prestamosActivos = contarPrestamosActivos(libro.getId());
        return libro.getCopias() > prestamosActivos;
    }

    /**
     * Crea un nuevo préstamo si existen copias disponibles del libro.
     *
     * @param libro libro a prestar
     * @param usuario usuario que realiza el préstamo
     * @param fechaDevolucion fecha prevista de devolución
     * @return préstamo creado o {@code null} si no hay copias disponibles
     */
    public Prestamo crear(Libro libro, Usuario usuario, LocalDate fechaDevolucion) {
        if (!hayCopiasDisponibles(libro)) {
            return null;
        }

        Prestamo prestamo = new Prestamo();
        prestamo.setLibro(libro);
        prestamo.setUsuario(usuario);
        prestamo.setFechaPrestamo(LocalDate.now());
        prestamo.setFechaDevolucion(fechaDevolucion);
        prestamo.setEstado(EstadoPrestamo.ACTIVO);
        return prestamoRepository.save(prestamo);
    }

    /**
     * Guarda o actualiza un préstamo.
     *
     * @param prestamo préstamo a persistir
     * @return préstamo guardado
     */
    public Prestamo save(Prestamo prestamo) {
        return prestamoRepository.save(prestamo);
    }

    /**
     * Elimina un préstamo por su identificador.
     *
     * @param id identificador del préstamo
     */
    public void deleteById(Long id) {
        prestamoRepository.deleteById(id);
    }

    /**
     * Marca un préstamo como devuelto.
     *
     * @param id identificador del préstamo
     * @return préstamo actualizado o {@code null} si no existe
     */
    public Prestamo devolver(Long id) {
        Optional<Prestamo> prestamo = prestamoRepository.findById(id);
        if (prestamo.isPresent()) {
            Prestamo p = prestamo.get();
            p.setEstado(EstadoPrestamo.DEVUELTO);
            return prestamoRepository.save(p);
        }
        return null;
    }

    /**
     * Actualiza el estado de una lista de préstamos vencidos.
     *
     * @param prestamos lista de préstamos a comprobar
     */
    private void actualizarVencidos(List<Prestamo> prestamos) {
        for (Prestamo p : prestamos) {
            actualizarVencido(p);
        }
    }

    /**
     * Actualiza el estado de un préstamo si ha superado la fecha de devolución.
     *
     * @param prestamo préstamo a comprobar
     */
    private void actualizarVencido(Prestamo prestamo) {
        if (prestamo.getEstado() == EstadoPrestamo.ACTIVO &&
                prestamo.getFechaDevolucion().isBefore(LocalDate.now())) {
            prestamo.setEstado(EstadoPrestamo.VENCIDO);
            prestamoRepository.save(prestamo);
        }
    }

}
