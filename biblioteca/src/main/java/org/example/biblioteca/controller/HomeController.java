package org.example.biblioteca.controller;

import org.springframework.ui.Model;
import org.example.biblioteca.model.Usuario;
import org.example.biblioteca.service.PrestamoService;
import org.example.biblioteca.service.UsuarioService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;

/**
 * Controlador principal de la aplicación.
 * <p>
 * Gestiona el acceso a la página de inicio.
 *
 *  @author Tatiana Cerezo
 *  @version 1.1
 */
@Controller
public class HomeController {

    /** Servicio de negocio para la gestión de usuarios */
    private final UsuarioService usuarioService;
    /** Servicio de negocio para la gestión de préstamos */
    private final PrestamoService prestamoService;

    /**
     * Constructor con inyección de dependencias.
     *
     * @param prestamoService servicio de préstamos
     * @param usuarioService servicio de usuarios
     */
    public HomeController(UsuarioService usuarioService, PrestamoService prestamoService) {
        this.usuarioService = usuarioService;
        this.prestamoService = prestamoService;
    }

    /**
     * Muestra la página principal de la aplicación.
     * <p>
     * Si el usuario es USER, muestra los préstamos pendientes (ACTIVOS Y VENCIDOS)
     *
     * @param model modelo para la vista
     * @param userDetails usuario autenticado
     * @return vista de inicio
     */
    @GetMapping("/")
    public String index(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails != null) {
            Optional<Usuario> usuario = usuarioService.findByEmail(userDetails.getUsername());
            if (usuario.isPresent() && usuario.get().getRol().name().equals("USER")) {
                model.addAttribute("prestamosActivos", prestamoService.findActivosYVencidosOrdenados(usuario.get().getId()));
            }
        }
        return "index";
    }
}
