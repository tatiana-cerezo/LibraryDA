package org.example.biblioteca.controller;

import org.example.biblioteca.model.*;
import org.example.biblioteca.service.LibroService;
import org.example.biblioteca.service.PrestamoService;
import org.example.biblioteca.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests del controlador de préstamos.
 *
 * @author Tatiana Cerezo
 * @version 1.0
 */
@WebMvcTest(PrestamoController.class)
public class PrestamoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PrestamoService prestamoService;

    @MockitoBean
    private LibroService libroService;

    @MockitoBean
    private UsuarioService usuarioService;

    private Prestamo prestamoActivo;
    private Prestamo prestamoVencido;
    private Prestamo prestamoDevuelto;

    @BeforeEach
    void setUp() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNombre("Test");
        usuario.setRol(Rol.ADMIN);

        Libro libro = new Libro();
        libro.setId(1L);
        libro.setTitulo("Test Libro");

        prestamoActivo = new Prestamo();
        prestamoActivo.setId(1L);
        prestamoActivo.setUsuario(usuario);
        prestamoActivo.setLibro(libro);
        prestamoActivo.setFechaPrestamo(LocalDate.now());
        prestamoActivo.setFechaDevolucion(LocalDate.now().plusDays(14));
        prestamoActivo.setEstado(EstadoPrestamo.ACTIVO);

        prestamoVencido = new Prestamo();
        prestamoVencido.setId(2L);
        prestamoVencido.setUsuario(usuario);
        prestamoVencido.setLibro(libro);
        prestamoVencido.setFechaPrestamo(LocalDate.now().minusDays(30));
        prestamoVencido.setFechaDevolucion(LocalDate.now().minusDays(10));
        prestamoVencido.setEstado(EstadoPrestamo.VENCIDO);

        prestamoDevuelto = new Prestamo();
        prestamoDevuelto.setId(3L);
        prestamoDevuelto.setUsuario(usuario);
        prestamoDevuelto.setLibro(libro);
        prestamoDevuelto.setFechaPrestamo(LocalDate.now().minusDays(20));
        prestamoDevuelto.setFechaDevolucion(LocalDate.now().minusDays(5));
        prestamoDevuelto.setEstado(EstadoPrestamo.DEVUELTO);
    }

    @Test
    @DisplayName("No permite eliminar préstamo activo")
    @WithMockUser(roles = "ADMIN")
    void eliminar_PrestamoActivo_NoElimina() throws Exception {
        when(prestamoService.findById(1L)).thenReturn(Optional.of(prestamoActivo));

        mockMvc.perform(get("/prestamos/eliminar/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/prestamos"))
                .andExpect(flash().attributeExists("error"));

        verify(prestamoService, never()).deleteById(1L);
    }

    @Test
    @DisplayName("No permite eliminar préstamo vencido")
    @WithMockUser(roles = "ADMIN")
    void eliminar_PrestamoVencido_NoElimina() throws Exception {
        when(prestamoService.findById(2L)).thenReturn(Optional.of(prestamoVencido));

        mockMvc.perform(get("/prestamos/eliminar/2"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/prestamos"))
                .andExpect(flash().attributeExists("error"));

        verify(prestamoService, never()).deleteById(2L);
    }

    @Test
    @DisplayName("Permite eliminar préstamo devuelto")
    @WithMockUser(roles = "ADMIN")
    void eliminar_PrestamoDevuelto_Elimina() throws Exception {
        when(prestamoService.findById(3L)).thenReturn(Optional.of(prestamoDevuelto));

        mockMvc.perform(get("/prestamos/eliminar/3"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/prestamos"))
                .andExpect(flash().attributeExists("mensaje"));

        verify(prestamoService, times(1)).deleteById(3L);
    }

}
