package org.example.biblioteca.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LegalController {

    @GetMapping("/legal")
    public String avisoLegal() {
        return "legal/aviso";
    }

    @GetMapping("/privacidad")
    public String privacidad() {
        return "legal/privacidad";
    }
}