package org.example.biblioteca.repository;

import org.example.biblioteca.model.EstadoPrestamo;
import org.example.biblioteca.model.Prestamo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio para gestionar operaciones de persistencia de préstamos.
 * Extiende JpaRepository para operaciones CRUD básicas.
 *
 * @author Tatiana Cerezo
 * @version 1.0
 */
@Repository
public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {

    List<Prestamo> findByUsuarioId(Long usuarioId);

    List<Prestamo> findByLibroId(Long libroId);

    List<Prestamo> findByEstado(EstadoPrestamo estado);

    List<Prestamo> findByLibroIdAndEstado(Long libroId, EstadoPrestamo estado);
}
