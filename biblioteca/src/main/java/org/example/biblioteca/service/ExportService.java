package org.example.biblioteca.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.example.biblioteca.dto.LibroDTO;
import org.example.biblioteca.model.Libro;
import org.example.biblioteca.repository.LibroRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * Servicio para exportar libros en formato JSON.
 *
 * @author Tatiana Cerezo
 * @version 1.0
 */
@Service
public class ExportService {

    private final LibroRepository libroRepository;
    private final ObjectMapper objectMapper;

    public ExportService(LibroRepository libroRepository) throws IOException {
        this.libroRepository = libroRepository;
        this.objectMapper = new ObjectMapper();

        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    /**
     * Exporta todos los libros a JSON.
     *
     * @return JSON con todos los libros
     * @throws JsonProcessingException si hay error al generar JSON
     */
    public String exportarLibros() throws JsonProcessingException {
        List<Libro> libros = libroRepository.findAll();
        List<LibroDTO> librosDTO = libros.stream()
                .map(LibroDTO::new)
                .toList();
        return objectMapper.writeValueAsString(librosDTO);
    }
}


