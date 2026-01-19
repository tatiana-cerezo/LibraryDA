# LIBRARYDA - MANUAL DE DESARROLLO

## Objetivo del Manual de Desarrollo
Proveer guías claras y procedimientos estandarizados para el desarrollo del proyecto "LibraryDA", asegurando la calidad del código y el correcto uso de las herramientas de control de versiones.

## Procedimientos
1. Creación de Ramas:

- Naming Convention: Las ramas deben seguir la estructura: tipo/descripcion. Ejemplos:
```
feature/gestion-libros
feature/gestion-prestamos
bugfix/corregir-error-login
hotfix/ajuste-seguridad
```

- Flujo de Trabajo:
    - Crear una nueva rama a partir de main.
    - Trabajar en la funcionalidad asignada y realizar commits frecuentes con mensajes claros y descriptivos.

2. Revisión de Código:

- Commits:
    - Cada commit debe tener un mensaje descriptivo del cambio realizado.
    - Agrupar cambios relacionados en un mismo commit.

- Verificación:
    - Antes de subir cambios, verificar que el código compila y funciona correctamente.
    - Revisar que se cumplen los estándares de codificación.

3. Estándares de Codificación
- Convenciones de Nombres:
    - Clases: PascalCase (ej: LibroController, PrestamoService).
    - Métodos y Variables: camelCase (ej: buscarLibroPorIsbn, fechaPrestamo).
    - Constantes: UPPER_SNAKE_CASE (ej: MAX_DIAS_PRESTAMO).
    - Paquetes: minúsculas (ej: com.biblioteca.controller).

- Estilo de Código:
    - Formato:
        - Indentación de 4 espacios.
        - Líneas de máximo 120 caracteres.

- Comentarios:
    - Usar // para comentarios de línea.
    - Usar /** */ para documentación JavaDoc en clases y métodos públicos.

- Buenas Prácticas:
    - Evitar código duplicado mediante métodos reutilizables.
    - Utilizar nombres descriptivos para clases, métodos y variables.
    - Mantener los métodos cortos y con una única responsabilidad.

4. Uso de Git
- Flujo de Trabajo:
    - Rama Principal (main): Contiene código estable y funcional.
    - Ramas de Característica (feature): Se crean para desarrollar funcionalidades específicas y luego se fusionan a main.

- Buenas Prácticas en Git:
    - Realizar commits frecuentes con mensajes significativos.
    - Hacer push regularmente para mantener el repositorio actualizado.
    - Estructura del mensaje: tipo: descripción breve. Ejemplos:
```
feat: añadir CRUD de libros
fix: corregir validación de ISBN
docs: actualizar README
```

5. Resolución de Conflictos
- Estrategias:
    - Antes de Fusionar:
        - Actualizar la rama local con los últimos cambios de main (git pull).
        - Resolver los conflictos localmente antes de hacer push.
    - Herramientas:
        - Usar el editor del IDE (IntelliJ, VS Code) para visualizar y resolver conflictos.
        - Interfaz web de GitHub para conflictos sencillos.
    - Buena Práctica: Si hay dudas sobre un conflicto, revisar el historial de cambios antes de decidir qué mantener.

6. Control de Versiones
- Etiquetas (Tags):  
Utilizar etiquetas para identificar versiones significativas del proyecto.

7. Indicadores de Calidad
- Métrica de cobertura de pruebas > 80%.
- Tiempo máximo para resolver un bug crítico: < 24 horas.