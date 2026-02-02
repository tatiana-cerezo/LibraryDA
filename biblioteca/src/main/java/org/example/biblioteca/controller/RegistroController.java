package org.example.biblioteca.controller;

import org.example.biblioteca.model.Rol;
import org.example.biblioteca.model.Usuario;
import org.example.biblioteca.service.EmailService;
import org.example.biblioteca.service.UsuarioService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.SecureRandom;

/**
 * Controlador para el registro de nuevos usuarios.
 *
 * @author Tatiana Cerezo
 * @version 1.1
 */
@Controller
@RequestMapping("/registro")
public class RegistroController {

    /** Servicio de negocio para la gestión de usuarios */
    private final UsuarioService usuarioService;
    /** Codificador de contraseñas para el almacenamiento seguro */
    private final PasswordEncoder passwordEncoder;
    /** Servicio para el envío de correos electrónicos */
    private final EmailService emailService;

    /**
     * Constructor con inyección de dependencias.
     *
     * @param usuarioService servicio de usuarios
     * @param passwordEncoder codificador de contraseñas
     */
    public RegistroController(UsuarioService usuarioService, PasswordEncoder passwordEncoder, EmailService emailService) {
        this.usuarioService = usuarioService;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    /**
     * Muestra el formulario de registro de usuarios
     *
     * @param model modelo para la vista
     * @return vista de listado de usuarios
     */
    @GetMapping
    public String mostrarFormulario(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "registro/formulario";
    }

    /**
     * Registra un nuevo usuario.
     * Envía mensaje con el resultado
     *
     * @param nombre nombre del usuario
     * @param email correo electrónico del usuario
     * @param redirectAttributes atributos para mensajes flash
     * @return redirección al registro si hay error y a login si tiene éxito
     */
    @PostMapping
    public String registrar(@RequestParam String nombre,
                            @RequestParam String email,
                            RedirectAttributes redirectAttributes) {

        if (usuarioService.existsByEmail(email)) {
            redirectAttributes.addFlashAttribute("error", "registro.email.existe");
            return "redirect:/registro";
        }

        String passwordGenerada = generarPassword();

        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setEmail(email);
        usuario.setPassword(passwordEncoder.encode(passwordGenerada));
        usuario.setRol(Rol.USER);

        usuarioService.save(usuario);

        emailService.enviarPassword(email, passwordGenerada);

        redirectAttributes.addFlashAttribute("mensaje", "registro.exito");
        return "redirect:/login";
    }

    /**
     * Genera contraseña aleatoria
     * con una longitud de 10 caracteres
     * que mezcla letras mayúsculas y minúsculas y números
     *
     * @return contraseña generada
     */
    private String generarPassword() {
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder(10);
        for (int i = 0; i < 10; i++) {
            password.append(caracteres.charAt(random.nextInt(caracteres.length())));
        }
        return password.toString();
    }

}
