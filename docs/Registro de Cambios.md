# LIBRARYDA - REGISTRO DE CAMBIOS (CHANGELOG)

## Propósito del Registro de Cambios
Documentar de manera detallada y estructurada las modificaciones realizadas en el proyecto, facilitando el seguimiento de avances e identificación de errores.

## Formato del Registro
Cada entrada del registro debe contener los siguientes campos:
- Fecha: Día en que se realizaron los cambios (formato DD-mm-YYYY).
- Versión: Siguiendo el esquema de versionado semántico (MAJOR.MINOR.PATCH).
- Cambios Realizados: Breve descripción de las modificaciones introducidas.
- Autor: Nombre del desarrollador responsable.


## Ejemplo de Registro
```
Versión 1.0.0

Fecha: 2025-01-27
Cambios Realizados:
- Desarrollo inicial del MVP.
- Implementación de entidades: Usuario, Libro, Préstamo.
- CRUD completo de las tres entidades.
- Configuración de Spring Security.
- Integración de la base de datos PostgreSQL con Hibernate.
Autor: Tatiana Cerezo

Versión 1.1.0

Fecha: 2025-02-03
Cambios Realizados:
- Implementación de multiidioma (español/inglés).
- Añadido aviso legal y política de cookies.
- Mejoras en la interfaz de usuario.
- Pruebas funcionales completadas.
Autor: Tatiana Cerezo

Versión 2.0.0

Fecha: 2025-02-10
Cambios Realizados:
- Entrega final del proyecto.
- Documentación completa.
- Configuración estándar aplicada (BD: acd_proyecto_2025, usuario: acd).
Autor: Tatiana Cerezo
```

## Lineamientos para el Registro de Cambios
*Versionado Semántico:*  
- MAJOR: Cambios significativos o incompatibles con versiones anteriores.
- MINOR: Nuevas funcionalidades que no afectan la compatibilidad.
- PATCH: Correcciones de errores y mejoras menores.

*Buenas Prácticas:*  
Utilizar descripciones claras y precisas para cada cambio.
Mantener el registro actualizado tras completar cada hito.


## Herramientas Recomendadas
- Control de Versiones:  
Uso de Git para generar registros mediante tags y commits.

- Documentación:  
Almacenar el registro en un archivo CHANGELOG.md en el repositorio del proyecto.