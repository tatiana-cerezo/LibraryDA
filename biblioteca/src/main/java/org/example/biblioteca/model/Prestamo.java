package org.example.biblioteca.model;

import jakarta.persistence.*;
import java.time.LocalDate;

/**
 * Entidad que representa un préstamo de libro en el sistema.
 * Relaciona un usuario con un libro durante un período de tiempo.
 *
 * @author Tatiana Cerezo
 * @version 1.0
 */
@Entity
@Table(name = "prestamos")
public class Prestamo {

    /** Identificador único del préstamo */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Usuario que realiza el préstamo */
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    /** Libro prestado */
    @ManyToOne
    @JoinColumn(name = "libro_id", nullable = false)
    private Libro libro;

    /** Fecha en que se realizó el préstamo */
    @Column(nullable = false)
    private LocalDate fechaPrestamo;

    /** Fecha límite para devolver el libro */
    @Column(nullable = false)
    private LocalDate fechaDevolucion;

    /** Estado actual del préstamo */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoPrestamo estado;

    public Prestamo() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Libro getLibro() {
        return libro;
    }

    public void setLibro(Libro libro) {
        this.libro = libro;
    }

    public LocalDate getFechaPrestamo() {
        return fechaPrestamo;
    }

    public void setFechaPrestamo(LocalDate fechaPrestamo) {
        this.fechaPrestamo = fechaPrestamo;
    }

    public LocalDate getFechaDevolucion() {
        return fechaDevolucion;
    }

    public void setFechaDevolucion(LocalDate fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
    }

    public EstadoPrestamo getEstado() {
        return estado;
    }

    public void setEstado(EstadoPrestamo estado) {
        this.estado = estado;
    }
}
