# LIBRARYDA - ESPECIFICACIONES TÉCNICAS

## ARQUITECTURA

El sistema estará basado en una arquitectura cliente-servidor y seguirá el patrón MVC (Modelo-Vista-Controlador) implementado con Spring Boot:  

*Cliente:*
- Navegador web responsive que renderiza las vistas HTML generadas por el servidor.
- Comunicación con el servidor mediante peticiones HTTP a una API REST.

*Servidor:*
- Backend desarrollado en Java con Spring Boot.
- Gestiona la lógica de negocio, seguridad y persistencia de datos.
- Integración con una base de datos relacional que guarda la información de una biblioteca.

*Modelo:*
- Entidades JPA que representan las tablas de la base de datos.
- Entidades principales: Usuario, Libro, Préstamo.
- Relaciones *OneToMany*: Usuario-Préstamos, Libro-Préstamos.

*Vista:*
- Plantillas Thymeleaf para renderizado del lado del servidor.
- HTML5 y CSS3 para estructura y estilos.

*Controlador:*
- Controladores Spring MVC para gestionar peticiones HTTP.
- Servicios que encapsulan la lógica de negocio.
- Repositorios Spring Data JPA para acceso a datos.

## TECNOLOGÍAS SELECCIONADAS

*Backend:*
- Lenguaje: Java 17+.
- Framework: Spring Boot (Spring MVC, Spring Data JPA).
- Seguridad: Spring Security.

*Frontend:*
- Lenguajes: HTML5, CSS3.
- Motor de plantillas: Thymeleaf.
- internacionalización: i18n.

*Base de datos:*
- Motor: MySQL 8.0.
- ORM: Hibernate.

*Herramientas:*
- IDE: IntelliJ IDEA y VS Code.
- Control de versiones: Git y GitHub.
- Gestión de tareas: Trello.

## ESTÁNDARES

*API REST:*
- Estandarización de endpoints siguiendo buenas prácticas.

*Seguridad:*
- Autenticación basada en tokens JWT (JSON Web Tokens).
- Cifrado de datos sensibles.

*Codificación:*
- Clases: PascalCase.
- Métodos y variables: camelCase.
- Constantes: UPPER_SNAKE_CASE.
- Comentarios JavaDoc en clases y métodos públicos.

*Estructura del proyecto:*
```
src/main/java/
├── controller/
├── model/
├── repository/
├── service/
└── config/

src/main/resources/
├── templates/
├── static/css/
├── messages_es.properties
├── messages_en.properties
└── application.properties
```

## INTERFAZ DE USUARIO

*Páginas principales:*
- Login / Registro.
- Listado de libros (con búsqueda).
- Detalle de libro.
- Gestión de préstamos.
- Panel de administración (usuarios).
- Aviso legal y política de privacidad.

*Características:*
- Diseño responsivo (móvil, tablet, escritorio).
- Selector de idioma (español/inglés).
- Banner de cookies.

## SEGURIDAD

*Autenticación:*
- Login con usuario y contraseña.
- Contraseñas cifradas con BCrypt.

*Autorización:*
- Roles con control de acceso: ADMIN, USER.
- Control de acceso por rutas.

*Protección:*
- CSRF habilitado.
- Validación de entrada de datos.

## ESCALABILIDAD Y RENDIMIENTO

*Escalabilidad:*
- Arquitectura MVC que permite separar responsabilidades y facilita futuras ampliaciones.
- Código modular organizado en capas (controller, service, repository).
- Uso de Spring Data JPA que permite cambiar de base de datos con mínimos cambios.

*Rendimiento:*
- Consultas optimizadas mediante Spring Data JPA.
- Carga de relaciones bajo demanda (Lazy Loading).
- Paginación en listados para evitar cargar todos los registros.

## INDICADORES DE RENDIMIENTO (KPIs)
- Tiempo de carga de páginas: < 3 segundos.
- Tiempo de respuesta de consultas a base de datos: < 500 ms.
- Disponibilidad del sistema en entorno local: 100% durante las pruebas.