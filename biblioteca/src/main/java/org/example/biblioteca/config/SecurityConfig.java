package org.example.biblioteca.config;

import org.example.biblioteca.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * Configuración de seguridad de la aplicación.
 * <p>
 * Define la autenticación, autorización, login, logout
 * y el uso de un {@link UserDetailsService} personalizado.
 *
 *  @author Tatiana Cerezo
 *  @version 1.2
 *
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /** Servicio personalizado de usuarios para Spring Security */
    private final CustomUserDetailsService userDetailsService;

    /**
     * Constructor con inyección de dependencias.
     *
     * @param userDetailsService servicio de detalles de usuario
     */
    public SecurityConfig(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    /**
     * Define el codificador de contraseñas.
     *
     * @return codificador BCrypt
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configura la cadena de filtros de seguridad.
     *
     * @param http configuración HTTP de Spring Security
     * @return cadena de filtros configurada
     * @throws Exception en caso de error de configuración
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/css/**", "/login/**", "/legal", "/privacidad", "/registro/**").permitAll()
                        .requestMatchers("/").permitAll()
                        .requestMatchers("/usuarios/**").authenticated()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/", true)
                        .failureUrl("/login?error")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/")
                        .permitAll()
                )
                .userDetailsService(userDetailsService);

        return http.build();
    }
}
