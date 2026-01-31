package org.example.biblioteca.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controlador de la página de autenticación.
 * <p>
 * Gestiona el acceso a la página de login.
 *
 *  @author Tatiana Cerezo
 *  @version 1.0
 */
@Controller
public class AuthController {

    /**
     * Muestra la página login.
     *
     * @return vista de login
     */
    @GetMapping("/login")
    public String login() {
        return "login";
    }
}