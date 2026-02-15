package org.example.biblioteca.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import org.example.biblioteca.dto.LibroDTO;
import org.example.biblioteca.model.Libro;
import org.example.biblioteca.repository.LibroRepository;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Set;

/**
 * Servicio para importar libros en formato JSON.
 *
 * @author Tatiana Cerezo
 * @version 1.0
 */
@Service
public class ImportService {

    private final LibroRepository libroRepository;
    private final ObjectMapper objectMapper;
    private final JsonSchema schema;

    public ImportService(LibroRepository libroRepository) throws IOException {
        this.libroRepository = libroRepository;

        this.objectMapper = new ObjectMapper();
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V202012);
        InputStream schemaStream = new ClassPathResource("schemas/libros-schema.json").getInputStream();
        this.schema = factory.getSchema(schemaStream);
    }

    /**
     * Importa libros desde JSON.
     *
     * @param json JSON con los libros a importar
     * @return número de libros importados
     * @throws IOException si hay error al leer JSON
     * @throws IllegalArgumentException si el JSON no es válido
     */
    public int importarLibros(String json) throws IOException {
        JsonNode jsonNode = objectMapper.readTree(json);

        Set<ValidationMessage> errores = schema.validate(jsonNode);
        if (!errores.isEmpty()) {
            errores.forEach(msg -> System.err.println("Error de schema: " + msg));
            throw new IllegalArgumentException("JSON inválido respecto al schema");
        }

        List<LibroDTO> librosDTO = objectMapper.readValue(json, new TypeReference<List<LibroDTO>>() {});
        List<Libro> libros = librosDTO.stream()
                .map(LibroDTO::toEntity)
                .toList();

        libroRepository.saveAll(libros);
        return libros.size();
    }
}
