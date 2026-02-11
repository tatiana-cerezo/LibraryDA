# Sistema de Gestión de Biblioteca

Sistema web para la gestión de una biblioteca desarrollado con Spring Boot como proyecto académico del ciclo formativo DAM.

## Descripción

Aplicación web que permite gestionar libros, usuarios y préstamos de una biblioteca. Incluye autenticación con roles (ADMIN/USER), internacionalización (español/inglés) y notificaciones por email.

## Características

- CRUD completo de libros, usuarios y préstamos
- Autenticación y autorización con Spring Security
- Roles: ADMIN (gestión completa) y USER (gestión limitada)
- Internacionalización (español e inglés)
- Envío de contraseñas por email
- Control de copias disponibles
- Gestión de préstamos vencidos

---

## Requisitos Previos

- **Java 17** o superior
- **Maven 3.8** o superior
- **PostgreSQL 14** o superior
- **IDE** (IntelliJ IDEA recomendado)

---

## Instalación

### 1. Clonar el repositorio

```bash
git clone <url-del-repositorio>
cd biblioteca
```

### 2. Configurar PostgreSQL

Accede a PostgreSQL como superusuario:

```bash
psql -U postgres
```

Ejecuta los siguientes comandos:

```sql
-- Crear usuario
CREATE USER acd WITH PASSWORD 'acd';

-- Crear base de datos
CREATE DATABASE acd_proyecto_2025 OWNER acd;

-- Dar permisos
GRANT ALL PRIVILEGES ON DATABASE acd_proyecto_2025 TO acd;

-- Conectar a la base de datos
\c acd_proyecto_2025

-- Dar permisos en el esquema public
GRANT ALL ON SCHEMA public TO acd;
```

### 3. Configurar Email (Mailtrap)

Para el envío de contraseñas por email, usamos Mailtrap (servidor de pruebas):

1. Crea una cuenta gratuita en [https://mailtrap.io](https://mailtrap.io)
2. Ve a **Transactional** → **Sandboxes** → **My Sandbox**
3. En **Integration**, selecciona **SMTP**
4. Copia las credenciales

### 4. Configurar application.properties

Edita el archivo `src/main/resources/application.properties`:

```properties
# Base de datos
spring.datasource.url=jdbc:postgresql://localhost:5432/acd_proyecto_2025
spring.datasource.username=acd
spring.datasource.password=acd
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

# Email (sustituir con tus credenciales de Mailtrap)
spring.mail.host=sandbox.smtp.mailtrap.io
spring.mail.port=2525
spring.mail.username=TU_USERNAME_MAILTRAP
spring.mail.password=TU_PASSWORD_MAILTRAP
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true

# Mensajes i18n
spring.messages.basename=messages
spring.messages.encoding=UTF-8
```

### 5. Compilar el proyecto

```bash
mvn clean install
```

### 6. Ejecutar la aplicación

```bash
mvn spring-boot:run
```

O desde IntelliJ: ejecutar la clase `BibliotecaApplication.java`

### 7. Acceder a la aplicación

Abre el navegador en: [http://localhost:8080](http://localhost:8080)

---

## Primer Uso

### Crear usuario administrador inicial

La primera vez que uses la aplicación, necesitas crear un usuario ADMIN directamente en la base de datos:

```bash
psql -U acd -d acd_proyecto_2025
```

```sql
INSERT INTO usuarios (nombre, email, password, rol) 
VALUES ('Administrador', 'admin@biblioteca.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZRGdjGj/A4VysLVDGN1BNnBOcXbYK', 'ADMIN');
```

**Credenciales del administrador:**
- Email: `admin@biblioteca.com`
- Contraseña: `admin123`

> Nota: La contraseña está encriptada con BCrypt. El valor mostrado corresponde a "admin123".

---

## Uso de la Aplicación

### Roles y Permisos

| Funcionalidad | ADMIN | USER |
|---------------|-------|------|
| Ver libros | ✅ | ✅ |
| Crear/Editar/Eliminar libros | ✅ | ❌ |
| Ver todos los préstamos | ✅ | ❌ (solo suyos) |
| Crear préstamos | ✅ | ✅ (solo para sí mismo) |
| Devolver préstamos | ✅ | ✅ |
| Eliminar préstamos | ✅ (solo devueltos) | ❌ |
| Ver todos los usuarios | ✅ | ❌ (solo su perfil) |
| Crear usuarios | ✅ | ❌ |
| Editar usuarios | ✅ | ❌ (solo su perfil) |
| Eliminar usuarios | ✅ | ❌ (solo a sí mismo) |

### Flujo de Registro

1. Usuario accede a "Registrarse"
2. Introduce nombre y email
3. Se genera contraseña aleatoria
4. Se envía contraseña por email
5. Usuario accede con sus credenciales

### Flujo de Préstamos

1. Usuario busca libro por título
2. Selecciona libro disponible
3. Indica días de préstamo
4. Sistema crea préstamo con estado ACTIVO
5. Al devolver, cambia a estado DEVUELTO
6. Si pasa la fecha, cambia a estado VENCIDO

---

## Estructura del Proyecto

```
biblioteca/
├── src/
│   ├── main/
│   │   ├── java/org/example/biblioteca/
│   │   │   ├── config/         # Configuración Security e i18n
│   │   │   ├── controller/     # Controladores MVC
│   │   │   ├── model/          # Entidades JPA
│   │   │   ├── repository/     # Repositorios Spring Data
│   │   │   └── service/        # Lógica de negocio
│   │   └── resources/
│   │       ├── templates/      # Vistas Thymeleaf
│   │       ├── static/css/     # Estilos CSS
│   │       └── messages*.properties  # Traducciones
│   └── test/                   # Tests unitarios
├── docs/                       # Documentación
├── pom.xml
├── README.md
└── CHANGELOG.md
```

---

## Tecnologías Utilizadas

- **Backend**: Spring Boot 3.x
- **Seguridad**: Spring Security
- **Persistencia**: Spring Data JPA + Hibernate
- **Base de datos**: PostgreSQL
- **Vistas**: Thymeleaf
- **Email**: Spring Mail + Mailtrap
- **Testing**: JUnit 5 + Mockito

---

## Comandos Útiles

```bash
# Compilar sin tests
mvn clean install -DskipTests

# Ejecutar tests
mvn test

# Ejecutar aplicación
mvn spring-boot:run

# Generar JAR
mvn clean package
```

---

## Solución de Problemas

### Error de conexión a PostgreSQL

Verifica que PostgreSQL está ejecutándose:
```bash
# Windows
pg_ctl status

# Linux/Mac
sudo systemctl status postgresql
```

### Error "permiso denegado al esquema public"

Ejecuta en PostgreSQL:
```sql
\c acd_proyecto_2025
GRANT ALL ON SCHEMA public TO acd;
ALTER DATABASE acd_proyecto_2025 OWNER TO acd;
```

### Los emails no llegan

1. Verifica las credenciales de Mailtrap en `application.properties`
2. Revisa la bandeja de entrada en Mailtrap (no en tu email real)

### Las tildes no se muestran correctamente

Verifica que los archivos `messages*.properties` estén en UTF-8 o usa códigos Unicode (\u00e1 para á, etc.)

---

## Autor

**Tatiana Cerezo**  
Proyecto académico - DAM  
Enero 2025

---

## Licencia

Este proyecto es de uso académico.