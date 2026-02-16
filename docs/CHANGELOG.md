# Changelog: Sistema de Gestión de Biblioteca

## Versión 0.1.0

### Fecha: 2025-01-20

### Cambios Realizados:
- Documentación inicial del proyecto.
- Documento de requisitos.
- Plan de proyecto.
- Especificaciones técnicas.
- Manual de desarrollo.
- Creación del repositorio y estructura inicial.


## Versión 1.0.0

### Fecha: 2025-01-22

### Cambios Realizados:
- Desarrollo inicial del MVP.
- Implementación de entidades: Usuario, Libro, Préstamo.
- Relaciones OneToMany: Usuario-Préstamos, Libro-Préstamos.
- CRUD completo de las tres entidades.
- Configuración de Spring Security (autenticación y autorización por roles).
- Integración de la base de datos PostgreSQL con Hibernate.
- Implementación de multiidioma (español/inglés).
- Añadido aviso legal, política de privacidad y banner de cookies.
- Interfaz de usuario con Thymeleaf y CSS.

### Autor:
- Tatiana Cerezo


## Versión 1.1.0

### Fecha: 2025-01-26

### Cambios Realizados:
- Diagrama Entidad-Relación completado.
- Diagrama de Clases UML completado.
- Documentación JavaDoc en entidades.
- Documentación de endpoints de la aplicación.
- Corrección de validación de préstamos (copias disponibles).
- Corrección de permisos por rol en préstamos.

### Autor:
- Tatiana Cerezo


## Versión 2.0.0

### Fecha: 2025-02-16

### Cambios Realizados:
- Separación de préstamos en tablas: activos/vencidos y devueltos.
- Préstamos activos del usuario mostrados en página de inicio.
- Buscador de libros en formulario de nuevo préstamo.
- Control de eliminación: libros, usuarios y préstamos con validaciones.
- Sistema de registro de usuarios con envío de contraseña por email.
- Usuario USER puede ver y editar su perfil.
- Usuario USER puede eliminarse a sí mismo.
- Exportación de libros a JSON.
- Importación de libros desde JSON con validación de schema.
- Validación de año de publicación (1900-2026).
- Mejoras visuales en formularios e inputs.
- Documentación completa: README, API_ENDPOINTS, resumen del proyecto.
- Tests unitarios para servicios y controladores.

### Autor:
- Tatiana Cerezo