package org.example.biblioteca.service;

import org.example.biblioteca.model.*;
import org.example.biblioteca.repository.PrestamoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios para PrestamoService.
 *
 * @author Tatiana Cerezo
 * @version 1.0
 */
@ExtendWith(MockitoExtension.class)
public class PrestamoServiceTest {

    @Mock
    private PrestamoRepository prestamoRepository;

    @InjectMocks
    private PrestamoService prestamoService;

    private Usuario usuario;
    private Libro libro;
    private Prestamo prestamoActivo;
    private Prestamo prestamoDevuelto;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNombre("Test User");
        usuario.setEmail("test@test.com");
        usuario.setRol(Rol.USER);

        libro = new Libro();
        libro.setId(1L);
        libro.setTitulo("Test Libro");
        libro.setAutor("Test Autor");
        libro.setCopias(2);

        prestamoActivo = new Prestamo();
        prestamoActivo.setId(1L);
        prestamoActivo.setUsuario(usuario);
        prestamoActivo.setLibro(libro);
        prestamoActivo.setFechaPrestamo(LocalDate.now());
        prestamoActivo.setFechaDevolucion(LocalDate.now().plusDays(14));
        prestamoActivo.setEstado(EstadoPrestamo.ACTIVO);

    }

    @Test
    @DisplayName("Crear préstamo cuando hay copias disponibles")
    void crearPrestamo_ConCopiasDisponibles_DevuelvePrestamo() {
        when(prestamoRepository.findByLibroIdAndEstado(libro.getId(), EstadoPrestamo.ACTIVO))
                .thenReturn(List.of());
        when(prestamoRepository.save(any(Prestamo.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Prestamo resultado = prestamoService.crear(libro, usuario, LocalDate.now().plusDays(14));

        assertNotNull(resultado);
        assertEquals(EstadoPrestamo.ACTIVO, resultado.getEstado());
        assertEquals(libro, resultado.getLibro());
        assertEquals(usuario, resultado.getUsuario());
        verify(prestamoRepository, times(1)).save(any(Prestamo.class));
    }

    @Test
    @DisplayName("No crear préstamo cuando no hay copias disponibles")
    void crearPrestamo_SinCopiasDisponibles_DevuelveNull() {
        Prestamo prestamo1 = new Prestamo();
        prestamo1.setEstado(EstadoPrestamo.ACTIVO);
        Prestamo prestamo2 = new Prestamo();
        prestamo2.setEstado(EstadoPrestamo.ACTIVO);

        when(prestamoRepository.findByLibroIdAndEstado(libro.getId(), EstadoPrestamo.ACTIVO))
                .thenReturn(Arrays.asList(prestamo1, prestamo2));

        Prestamo resultado = prestamoService.crear(libro, usuario, LocalDate.now().plusDays(14));

        assertNull(resultado);
        verify(prestamoRepository, never()).save(any(Prestamo.class));
    }

    @Test
    @DisplayName("Devolver préstamo cambia estado a DEVUELTO")
    void devolverPrestamo_PrestamoExiste_CambiaEstado() {
        when(prestamoRepository.findById(1L)).thenReturn(Optional.of(prestamoActivo));
        when(prestamoRepository.save(any(Prestamo.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Prestamo resultado = prestamoService.devolver(1L);

        assertNotNull(resultado);
        assertEquals(EstadoPrestamo.DEVUELTO, resultado.getEstado());
        verify(prestamoRepository, times(1)).save(prestamoActivo);
    }

}
