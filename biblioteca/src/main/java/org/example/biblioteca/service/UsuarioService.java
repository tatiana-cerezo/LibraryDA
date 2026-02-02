package org.example.biblioteca.service;

import org.example.biblioteca.model.EstadoPrestamo;
import org.example.biblioteca.model.Prestamo;
import org.example.biblioteca.model.Usuario;
import org.example.biblioteca.repository.PrestamoRepository;
import org.example.biblioteca.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Servicio de negocio para la gestión de usuarios.
 * <p>
 * Proporciona operaciones CRUD y consultas básicas relacionadas
 * con los usuarios del sistema.
 *
 *  @author Tatiana Cerezo
 *  @version 1.1
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

    /**
     * Verifica si un usuario puede ser eliminado.
     * Solo puede eliminarse si no tiene préstamos activos o vencidos.
     *
     * @param usuarioId ID del usuario
     * @return true si puede eliminarse
     */
    public boolean puedeEliminarse(Long usuarioId) {
        Optional<Usuario> usuario = usuarioRepository.findById(usuarioId);
        if (usuario.isEmpty()) {
            return false;
        }

        List<Prestamo> prestamos = usuario.get().getPrestamos();
        if (prestamos == null) {
            return true;
        }

        return prestamos.stream()
                .noneMatch(p -> p.getEstado() == EstadoPrestamo.ACTIVO || p.getEstado() == EstadoPrestamo.VENCIDO);
    }

    /**
     * Elimina un usuario y sus préstamos devueltos asociados.
     *
     * @param id ID del usuario a eliminar
     * @param prestamoRepository repositorio de préstamos
     * @return true si se eliminó correctamente
     */
    public boolean eliminarConPrestamos(Long id, PrestamoRepository prestamoRepository) {
        if (!puedeEliminarse(id)) {
            return false;
        }

        Optional<Usuario> usuario = usuarioRepository.findById(id);
        if (usuario.isPresent() && usuario.get().getPrestamos() != null) {
            for (Prestamo p : usuario.get().getPrestamos()) {
                if (p.getEstado() == EstadoPrestamo.DEVUELTO) {
                    prestamoRepository.deleteById(p.getId());
                }
            }
        }

        usuarioRepository.deleteById(id);
        return true;
    }
}
