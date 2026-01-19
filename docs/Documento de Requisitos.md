# LIBRARYDA - DOCUMENTO DE REQUISITOS

El proyecto LIBRARYDA consiste en el desarrollo de una aplicación web para la gestión de una biblioteca. El sistema permitirá administrar el catálogo de libros, gestionar usuarios y controlar los préstamos realizados. Se implementarán relaciones *OneToMany* entre las entidades: un libro puede tener múltiples préstamos asociados y un usuario puede realizar múltiples préstamos.  
  
Como objetivos tenemos los siguientes:  
- Desarrollar una aplicación web que permita gestionar de forma eficiente los libros, usuarios y préstamos de una biblioteca.  
- Implementar autenticación y autorización de usuarios con diferentes roles.  
- Crear un CRUD completo para libros, usuarios y préstamos.  
- Establecer relaciones *OneToMany* entre Libro-Préstamo y Usuario-Préstamo.  
- Cumplir con requisitos legales (RGPD, política de cookies).  
- Implementar localización multiidioma (español e inglés).  
  
En cuanto a los requisitos funcionales del proyecto, son:
-	Gestión de Usuarios:  
    - Registro e inicio de sesión.  
    - Roles: Administrador y Usuario.  
    - CRUD de usuarios (solo administrador).  
-	Gestión de Libros:  
    - Alta, baja, modificación y consulta de libros.  
    - Campos: título, autor, ISBN, editorial, año, categoría, copias disponibles.  
    - Búsqueda por título, autor o categoría.  
-	Gestión de Préstamos:  
    - Registrar préstamos (usuario, libro, fecha inicio, fecha devolución prevista) y devoluciones.  
    - Consulta de préstamos activos e histórico.  
    - Un libro puede tener muchos préstamos (*OneToMany*) y un usuario puede tener muchos préstamos (*OneToMany*).  
-	Multiidioma:  
    - Interfaz disponible en español e inglés y selector de idioma accesible en todas las páginas.  
-	Requisitos Legales:  
    - Aviso legal y política de privacidad.  
    - Banner de consentimiento de cookies. 
   
Con respecto a los requisitos no funcionales encontramos:
-	Seguridad:  
    - Autenticación con Spring Security.  
    - Contraseñas cifradas con BCrypt.  
    - Protección CSRF activa.  
-	Rendimiento:  
    - Tiempo de carga inferior a 3 segundos.  
-	Compatibilidad:  
    - Diseño responsivo (móvil, tablet, escritorio).  
    - Compatible con Chrome, Firefox, Edge.  
  
Finalmente, las restricciones que nos encontramos son el tiempo limitado de desarrollo (4 semanas), y que el proyecto solo lo desarrolla una persona.
