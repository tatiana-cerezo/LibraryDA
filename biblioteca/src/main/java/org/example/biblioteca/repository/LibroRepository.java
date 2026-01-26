package org.example.biblioteca.repository;

import org.example.biblioteca.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio para gestionar operaciones de persistencia de libros.
 * Extiende JpaRepository para operaciones CRUD básicas.
 *
 * @author Tatiana Cerezo
 * @version 1.0
 */
@Repository
public interface LibroRepository extends JpaRepository<Libro, Long> {

    List<Libro> findByTituloContainingIgnoreCase(String titulo);

    List<Libro> findByAutorContainingIgnoreCase(String autor);

    List<Libro> findByCategoria(String categoria);
}
