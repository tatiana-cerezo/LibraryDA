package org.example.biblioteca.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controlador principal de la aplicación.
 * <p>
 * Gestiona el acceso a la página de inicio.
 */
@Controller
public class HomeController {

    /**
     * Muestra la página principal de la aplicación.
     *
     * @return vista de inicio
     */
    @GetMapping("/")
    public String index() {
        return "index";
    }
}
