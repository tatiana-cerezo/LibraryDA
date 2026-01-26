package org.example.biblioteca.repository;

import org.example.biblioteca.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio para gestionar operaciones de persistencia de usuarios.
 * Extiende JpaRepository para operaciones CRUD básicas.
 *
 * @author Tatiana Cerezo
 * @version 1.0
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);

    boolean existsByEmail(String email);
}
