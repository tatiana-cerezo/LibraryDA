package org.example.biblioteca.model;

import jakarta.persistence.*;
import java.util.List;

/**
 * Entidad que representa un usuario del sistema de biblioteca.
 * Los usuarios pueden tener rol ADMIN o USER y realizar préstamos de libros.
 *
 * @author Tatiana Cerezo
 * @version 1.0
 */
@Entity
@Table(name = "usuarios")
public class Usuario {

    /** Identificador único del usuario */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Nombre completo del usuario */
    @Column(nullable = false)
    private String nombre;

    /** Email del usuario, usado para autenticación */
    @Column(nullable = false, unique = true)
    private String email;

    /** Contraseña encriptada del usuario */
    @Column(nullable = false)
    private String password;

    /** Rol del usuario en el sistema */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Rol rol;

    /** Lista de préstamos realizados por el usuario */
    @OneToMany(mappedBy = "usuario")
    private List<Prestamo> prestamos;

    public Usuario() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public List<Prestamo> getPrestamos() {
        return prestamos;
    }

    public void setPrestamos(List<Prestamo> prestamos) {
        this.prestamos = prestamos;
    }
}
