package org.example.biblioteca.service;

import org.example.biblioteca.model.Usuario;
import org.example.biblioteca.repository.UsuarioRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * Implementación personalizada de {@link UserDetailsService} para Spring Security.
 * <p>
 * Se encarga de cargar los datos de autenticación del usuario a partir
 * de su email, adaptando la entidad {@link Usuario} al modelo de seguridad
 * de Spring.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    /** Repositorio para acceder a los usuarios */
    private final UsuarioRepository usuarioRepository;

    /**
     * Constructor con inyección de dependencias.
     *
     * @param usuarioRepository repositorio de usuarios
     */
    public CustomUserDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Carga un usuario por su email para el proceso de autenticación.
     *
     * @param email email del usuario
     * @return detalles del usuario para Spring Security
     * @throws UsernameNotFoundException si el usuario no existe
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + email));

        return new User(
                usuario.getEmail(),
                usuario.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + usuario.getRol().name()))
        );
    }
}