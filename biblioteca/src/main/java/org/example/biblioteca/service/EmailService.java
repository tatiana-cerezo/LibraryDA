package org.example.biblioteca.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Servicio para envío de correos electrónicos.
 *
 * @author Tatiana Cerezo
 * @version 1.0
 */
@Service
public class EmailService {

    /** Interfaz de Spring para envío de correos */
    private final JavaMailSender mailSender;

    /**
     * Constructor con inyección de dependencias.
     *
     * @param mailSender interfaz de envío de correos
     */
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * Envía un correo con la contraseña generada.
     *
     * @param destinatario email del destinatario
     * @param password contraseña generada
     */
    public void enviarPassword(String destinatario, String password) {
        SimpleMailMessage mensaje = new SimpleMailMessage();
        mensaje.setTo(destinatario);
        mensaje.setSubject("Biblioteca - Tu contraseña de acceso");
        mensaje.setText("Bienvenido a la Biblioteca.\n\n" +
                "Tu contraseña de acceso es: " + password + "\n\n" +
                "Por favor, cambia tu contraseña después de iniciar sesión.");
        mensaje.setFrom("libraryDA@ejemplo.com");

        mailSender.send(mensaje);
    }

}
