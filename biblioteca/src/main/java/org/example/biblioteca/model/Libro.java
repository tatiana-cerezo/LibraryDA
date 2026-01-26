package org.example.biblioteca.model;

import jakarta.persistence.*;
import java.util.List;

/**
 * Entidad que representa un libro en el sistema de biblioteca.
 * Cada libro puede tener múltiples copias y ser prestado a usuarios.
 *
 * @author Tatiana Cerezo
 * @version 1.0
 */
@Entity
@Table(name = "libros")
public class Libro {

    /** Identificador único del libro */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Título del libro */
    @Column(nullable = false)
    private String titulo;

    /** Autor del libro */
    @Column(nullable = false)
    private String autor;

    /** Editorial que publicó el libro */
    private String editorial;

    /** Año de publicación del libro */
    private Integer anioPublicacion;

    /** Categoría o género del libro */
    private String categoria;

    /** Número total de copias disponibles en la biblioteca */
    @Column(nullable = false)
    private Integer copias;

    /** Lista de préstamos asociados a este libro */
    @OneToMany(mappedBy = "libro")
    private List<Prestamo> prestamos;

    public Libro() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getEditorial() {
        return editorial;
    }

    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }

    public Integer getAnioPublicacion() {
        return anioPublicacion;
    }

    public void setAnioPublicacion(Integer anioPublicacion) {
        this.anioPublicacion = anioPublicacion;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Integer getCopias() {
        return copias;
    }

    public void setCopias(Integer copias) {
        this.copias = copias;
    }

    public List<Prestamo> getPrestamos() {
        return prestamos;
    }

    public void setPrestamos(List<Prestamo> prestamos) {
        this.prestamos = prestamos;
    }
}