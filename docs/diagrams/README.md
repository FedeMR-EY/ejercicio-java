# Diagramas del Proyecto

Este directorio contiene los diagramas del proyecto en formato Mermaid (.mmd).

## Archivos

| Archivo | Descripción |
|---------|-------------|
| `class-diagram.mmd` | Diagrama de clases completo del proyecto |
| `entity-relationship.mmd` | Diagrama Entidad-Relación de la base de datos |
| `sequence-diagram.mmd` | Diagrama de secuencia del flujo de registro |
| `sequence-login.mmd` | Diagrama de secuencia del flujo de login |
| `sequence-jwt-validation.mmd` | Diagrama de secuencia de validación JWT |

## Cómo visualizar los diagramas

### Opción 1: VS Code con extensión Mermaid
1. Instalar la extensión "Mermaid Preview" o "Markdown Preview Mermaid Support"
2. Abrir el archivo .mmd
3. Usar el preview (Ctrl+Shift+V)

### Opción 2: Mermaid Live Editor (Online)
1. Ir a https://mermaid.live/
2. Copiar el contenido del archivo .mmd
3. El diagrama se renderizará automáticamente
4. Exportar como PNG o SVG

### Opción 3: CLI de Mermaid (para generar PNG/SVG)
```bash
# Instalar mermaid-cli
npm install -g @mermaid-js/mermaid-cli

# Generar PNG
mmdc -i class-diagram.mmd -o class-diagram.png
mmdc -i entity-relationship.mmd -o entity-relationship.png
mmdc -i sequence-diagram.mmd -o sequence-diagram.png
mmdc -i sequence-login.mmd -o sequence-login.png
mmdc -i sequence-jwt-validation.mmd -o sequence-jwt-validation.png
```

### Opción 4: IntelliJ IDEA
1. Instalar el plugin "Mermaid" desde File > Settings > Plugins
2. Abrir el archivo .mmd
3. Ver el preview en el panel derecho

## Descripción de los diagramas

### Diagrama de Clases
Muestra la estructura completa del proyecto incluyendo:
- Interfaces (`AuthResource`, `DatabaseService`, `UserDetailsService`)
- Entidades JPA (`User`, `Phone`)
- DTOs de Request y Response
- Controladores y Servicios
- Configuración de Seguridad
- Manejo de Excepciones

### Diagrama Entidad-Relación
Representa el modelo de datos:
- **USERS**: Tabla principal de usuarios
- **PHONES**: Tabla de teléfonos con relación 1:N hacia USERS

### Diagramas de Secuencia
Muestran los flujos principales:
1. **Registro**: Creación de nuevo usuario con validación y generación de JWT
2. **Login**: Autenticación de usuario y generación de nuevo token
3. **Validación JWT**: Proceso de validación del token en requests protegidas
