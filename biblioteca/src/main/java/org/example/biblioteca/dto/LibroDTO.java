package org.example.biblioteca.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.example.biblioteca.model.Libro;

/**
 * DTO para exportar e importar libros en JSON.
 *
 * @author Tatiana Cerezo
 * @version 1.0
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LibroDTO {
    private String titulo;
    private String autor;
    private String editorial;
    private Integer anioPublicacion;
    private String categoria;
    private Integer copias;

    public LibroDTO() {}

    public LibroDTO(Libro libro) {
        this.titulo = libro.getTitulo();
        this.autor = libro.getAutor();
        this.editorial = libro.getEditorial();
        this.anioPublicacion = libro.getAnioPublicacion();
        this.categoria = libro.getCategoria();
        this.copias = libro.getCopias();
    }

    public Libro toEntity() {
        Libro libro = new Libro();
        libro.setTitulo(this.titulo);
        libro.setAutor(this.autor);
        libro.setEditorial(this.editorial);
        libro.setAnioPublicacion(this.anioPublicacion);
        libro.setCategoria(this.categoria);
        libro.setCopias(this.copias);
        return libro;
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
}
