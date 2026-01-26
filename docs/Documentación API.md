# Documentación de API (endpoints) - Sistema de Gestión de Biblioteca

## Información General

- **Base URL:** `http://localhost:8080`
- **Autenticación:** Spring Security con sesiones
- **Roles:** ADMIN, USER

---

## Endpoints Públicos (sin autenticación)

| Método | Ruta | Descripción | Vista |
|--------|------|-------------|-------|
| GET | `/` | Página de inicio | index.html |
| GET | `/login` | Formulario de login | login.html |
| POST | `/login` | Procesar login | Redirección |
| GET | `/logout` | Cerrar sesión | Redirección |
| GET | `/legal` | Aviso legal | legal/aviso.html |
| GET | `/privacidad` | Política de privacidad | legal/privacidad.html |

---

## Endpoints de Libros (requiere autenticación)

| Método | Ruta | Descripción | Rol | Vista | Parámetros/Atributos |
|--------|------|-------------|-----|-------|----------------------|
| GET | `/libros` | Listado de libros | USER, ADMIN | libros/listar.html | Model: libros |
| GET | `/libros/nuevo` | Formulario nuevo libro | USER, ADMIN | libros/formulario.html | Model: libro |
| POST | `/libros/guardar` | Guardar libro | USER, ADMIN | Redirección | @ModelAttribute: libro |
| GET | `/libros/editar/{id}` | Formulario editar libro | USER, ADMIN | libros/formulario.html | @PathVariable: id, Model: libro |
| GET | `/libros/eliminar/{id}` | Eliminar libro | USER, ADMIN | Redirección | @PathVariable: id |
| GET | `/libros/buscar` | Buscar libros por título | USER, ADMIN | libros/listar.html | @RequestParam: titulo, Model: libros |

---

## Endpoints de Usuarios (solo ADMIN)

| Método | Ruta | Descripción | Rol | Vista | Parámetros/Atributos |
|--------|------|-------------|-----|-------|----------------------|
| GET | `/usuarios` | Listado de usuarios | ADMIN | usuarios/listar.html | Model: usuarios |
| GET | `/usuarios/nuevo` | Formulario nuevo usuario | ADMIN | usuarios/formulario.html | Model: usuario, roles |
| POST | `/usuarios/guardar` | Guardar usuario | ADMIN | Redirección | @ModelAttribute: usuario |
| GET | `/usuarios/editar/{id}` | Formulario editar usuario | ADMIN | usuarios/formulario.html | @PathVariable: id, Model: usuario, roles |
| GET | `/usuarios/eliminar/{id}` | Eliminar usuario | ADMIN | Redirección | @PathVariable: id |

---

## Endpoints de Préstamos (requiere autenticación)

| Método | Ruta | Descripción | Rol | Vista | Parámetros/Atributos |
|--------|------|-------------|-----|-------|----------------------|
| GET | `/prestamos` | Listado de préstamos | USER (solo propios), ADMIN (todos) | prestamos/listar.html | Model: prestamos |
| GET | `/prestamos/nuevo` | Formulario nuevo préstamo | USER, ADMIN | prestamos/formulario.html | Model: libros, usuarios/usuarioActual, esAdmin |
| POST | `/prestamos/guardar` | Guardar préstamo | USER, ADMIN | Redirección | @RequestParam: libroId, usuarioId, diasPrestamo |
| GET | `/prestamos/devolver/{id}` | Devolver préstamo | USER, ADMIN | Redirección | @PathVariable: id |
| GET | `/prestamos/eliminar/{id}` | Eliminar préstamo | ADMIN | Redirección | @PathVariable: id |

---

## Reglas de Acceso

### Rutas públicas (permitAll)
- `/css/**`
- `/login/**`
- `/legal`
- `/privacidad`
- `/`

### Rutas restringidas por rol
- `/usuarios/**` → Solo ADMIN
- `/libros/**` → USER y ADMIN
- `/prestamos/**` → USER y ADMIN (con restricciones)

### Restricciones adicionales en préstamos
- USER solo ve sus propios préstamos
- USER solo puede crear préstamos para sí mismo
- USER puede devolver préstamos
- USER no puede eliminar préstamos
- ADMIN tiene acceso completo

---

## Parámetro de Idioma

Todas las rutas aceptan el parámetro `lang` para cambiar el idioma:
- `?lang=es` → Español
- `?lang=en` → Inglés

---

## Códigos de Respuesta

| Código | Descripción |
|--------|-------------|
| 200 | OK - Petición exitosa |
| 302 | Redirect - Redirección tras acción |
| 401 | Unauthorized - No autenticado |
| 403 | Forbidden - Sin permisos |
| 404 | Not Found - Recurso no encontrado |