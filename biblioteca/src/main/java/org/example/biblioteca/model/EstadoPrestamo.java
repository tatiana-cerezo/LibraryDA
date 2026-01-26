package org.example.biblioteca.model;

/**
 * Enumeración que define los estados posibles de un préstamo.
 *
 * @author Tatiana Cerezo
 * @version 1.0
 */
public enum EstadoPrestamo {
    /** Préstamo en curso, libro no devuelto */
    ACTIVO,
    /** Libro devuelto correctamente */
    DEVUELTO,
    /** Fecha de devolución superada sin devolver */
    VENCIDO
}
