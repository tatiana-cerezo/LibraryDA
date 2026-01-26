package org.example.biblioteca.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controlador de la página de contenido legal.
 * <p>
 * Gestiona el acceso a la página de legal.
 */
@Controller
public class LegalController {

    /**
     * Muestra la página de aviso legal.
     *
     * @return vista de legal/aviso
     */
    @GetMapping("/legal")
    public String avisoLegal() {
        return "legal/aviso";
    }

    /**
     * Muestra la página de información sobre la privacidad.
     *
     * @return vista de privacidad
     */
    @GetMapping("/privacidad")
    public String privacidad() {
        return "legal/privacidad";
    }
}