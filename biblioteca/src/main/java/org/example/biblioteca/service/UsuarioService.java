package org.example.biblioteca.service;

import org.example.biblioteca.model.Usuario;
import org.example.biblioteca.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Servicio de negocio para la gestión de usuarios.
 * <p>
 * Proporciona operaciones CRUD y consultas básicas relacionadas
 * con los usuarios del sistema.
 */
@Service
public class UsuarioService {

    /** Repositorio para el acceso y persistencia de usuarios */
    private final UsuarioRepository usuarioRepository;

    /**
     * Constructor con inyección de dependencias.
     *
     * @param usuarioRepository repositorio de usuarios
     */
    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Obtiene todos los usuarios del sistema.
     *
     * @return lista de usuarios
     */
    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    /**
     * Busca un usuario por su identificador.
     *
     * @param id identificador del usuario
     * @return usuario encontrado o vacío si no existe
     */
    public Optional<Usuario> findById(Long id) {
        return usuarioRepository.findById(id);
    }

    /**
     * Busca un usuario por su email.
     *
     * @param email email del usuario
     * @return usuario encontrado o vacío si no existe
     */
    public Optional<Usuario> findByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    /**
     * Guarda o actualiza un usuario.
     *
     * @param usuario usuario a persistir
     * @return usuario guardado
     */
    public Usuario save(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    /**
     * Elimina un usuario por su identificador.
     *
     * @param id identificador del usuario
     */
    public void deleteById(Long id) {
        usuarioRepository.deleteById(id);
    }

    /**
     * Comprueba si existe un usuario con el email indicado.
     *
     * @param email email a comprobar
     * @return {@code true} si existe, {@code false} en caso contrario
     */
    public boolean existsByEmail(String email) {
        return usuarioRepository.existsByEmail(email);
    }
}
