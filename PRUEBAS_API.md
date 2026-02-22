# Guía de pruebas de la API

Esta guía te ayudará a probar todos los endpoints del backend.

## Herramientas recomendadas

- **Postman** (recomendado): https://www.postman.com/downloads/
- **curl** (línea de comandos)
- **Thunder Client** (extensión de VS Code)
- **REST Client** (extensión de VS Code)

## Configuración base

**Base URL:** `http://localhost:8080/api`

**Headers necesarios:**
```
Content-Type: application/json
```

## 1. Estados

### Crear estado
```bash
POST http://localhost:8080/api/estados
Content-Type: application/json

{
  "nombre": "Activo",
  "descripcion": "Estado activo"
}
```

**curl:**
```bash
curl -X POST http://localhost:8080/api/estados \
  -H "Content-Type: application/json" \
  -d "{\"nombre\":\"Activo\",\"descripcion\":\"Estado activo\"}"
```

### Listar todos los estados
```bash
GET http://localhost:8080/api/estados
```

**curl:**
```bash
curl http://localhost:8080/api/estados
```

### Obtener estado por ID
```bash
GET http://localhost:8080/api/estados/1
```

### Actualizar estado
```bash
PUT http://localhost:8080/api/estados/1
Content-Type: application/json

{
  "nombre": "Activo Modificado",
  "descripcion": "Descripción actualizada"
}
```

### Eliminar estado
```bash
DELETE http://localhost:8080/api/estados/1
```

## 2. Empresas

### Crear empresa
```bash
POST http://localhost:8080/api/empresas
Content-Type: application/json

{
  "nombre": "TechCorp S.A.",
  "descripcion": "Empresa de tecnología",
  "correo": "contacto@techcorp.com",
  "estadoId": 1
}
```

**curl:**
```bash
curl -X POST http://localhost:8080/api/empresas \
  -H "Content-Type: application/json" \
  -d "{\"nombre\":\"TechCorp S.A.\",\"descripcion\":\"Empresa de tecnología\",\"correo\":\"contacto@techcorp.com\",\"estadoId\":1}"
```

### Listar empresas
```bash
GET http://localhost:8080/api/empresas
```

**Respuesta esperada:**
```json
[
  {
    "id": 1,
    "nombre": "TechCorp S.A.",
    "descripcion": "Empresa de tecnología",
    "correo": "contacto@techcorp.com",
    "estadoId": 1,
    "estadoNombre": "Activo"
  }
]
```

## 3. Tipos de Proyecto

### Crear tipo de proyecto
```bash
POST http://localhost:8080/api/tipos-proyecto
Content-Type: application/json

{
  "nombre": "Software",
  "descripcion": "Desarrollo de software",
  "estadoId": 1
}
```

### Listar tipos de proyecto
```bash
GET http://localhost:8080/api/tipos-proyecto
```

## 4. Proyectos

### Crear proyecto
```bash
POST http://localhost:8080/api/proyectos
Content-Type: application/json

{
  "nombre": "Sistema ERP",
  "descripcion": "Sistema de planificación empresarial",
  "empresaId": 1,
  "tipoProyectoId": 1,
  "estadoId": 3
}
```

**curl:**
```bash
curl -X POST http://localhost:8080/api/proyectos \
  -H "Content-Type: application/json" \
  -d "{\"nombre\":\"Sistema ERP\",\"descripcion\":\"Sistema de planificación empresarial\",\"empresaId\":1,\"tipoProyectoId\":1,\"estadoId\":3}"
```

### Listar proyectos
```bash
GET http://localhost:8080/api/proyectos
```

**Respuesta esperada:**
```json
[
  {
    "id": 1,
    "nombre": "Sistema ERP",
    "descripcion": "Sistema de planificación empresarial",
    "empresaId": 1,
    "empresaNombre": "TechCorp S.A.",
    "tipoProyectoId": 1,
    "tipoProyectoNombre": "Software",
    "estadoId": 3,
    "estadoNombre": "En Proceso"
  }
]
```

## 5. Fases

### Crear fase
```bash
POST http://localhost:8080/api/fases
Content-Type: application/json

{
  "nombre": "Análisis",
  "descripcion": "Fase de análisis de requisitos",
  "estadoId": 1
}
```

### Listar fases
```bash
GET http://localhost:8080/api/fases
```

## 6. Sistemas

### Crear sistema
```bash
POST http://localhost:8080/api/sistemas
Content-Type: application/json

{
  "nombre": "Sistema Core",
  "descripcion": "Sistema principal",
  "estadoId": 1
}
```

### Listar sistemas
```bash
GET http://localhost:8080/api/sistemas
```

## 7. Subsistemas

### Crear subsistema
```bash
POST http://localhost:8080/api/subsistemas
Content-Type: application/json

{
  "nombre": "Módulo Usuarios",
  "descripcion": "Gestión de usuarios",
  "sistemaId": 1,
  "estadoId": 1
}
```

### Listar subsistemas
```bash
GET http://localhost:8080/api/subsistemas
```

**Respuesta esperada:**
```json
[
  {
    "id": 1,
    "nombre": "Módulo Usuarios",
    "descripcion": "Gestión de usuarios",
    "sistemaId": 1,
    "sistemaNombre": "Sistema Core",
    "estadoId": 1,
    "estadoNombre": "Activo"
  }
]
```

## 8. Ramas

### Crear rama
```bash
POST http://localhost:8080/api/ramas
Content-Type: application/json

{
  "nombre": "main",
  "descripcion": "Rama principal"
}
```

### Listar ramas
```bash
GET http://localhost:8080/api/ramas
```

## Script completo de prueba

Aquí un script completo para probar todo el flujo:

```bash
# 1. Crear estados
curl -X POST http://localhost:8080/api/estados -H "Content-Type: application/json" -d "{\"nombre\":\"Activo\",\"descripcion\":\"Estado activo\"}"
curl -X POST http://localhost:8080/api/estados -H "Content-Type: application/json" -d "{\"nombre\":\"Inactivo\",\"descripcion\":\"Estado inactivo\"}"
curl -X POST http://localhost:8080/api/estados -H "Content-Type: application/json" -d "{\"nombre\":\"En Proceso\",\"descripcion\":\"En proceso\"}"

# 2. Crear empresa
curl -X POST http://localhost:8080/api/empresas -H "Content-Type: application/json" -d "{\"nombre\":\"TechCorp\",\"descripcion\":\"Empresa tech\",\"correo\":\"info@techcorp.com\",\"estadoId\":1}"

# 3. Crear tipo de proyecto
curl -X POST http://localhost:8080/api/tipos-proyecto -H "Content-Type: application/json" -d "{\"nombre\":\"Software\",\"descripcion\":\"Desarrollo software\",\"estadoId\":1}"

# 4. Crear proyecto
curl -X POST http://localhost:8080/api/proyectos -H "Content-Type: application/json" -d "{\"nombre\":\"Sistema ERP\",\"descripcion\":\"ERP empresarial\",\"empresaId\":1,\"tipoProyectoId\":1,\"estadoId\":3}"

# 5. Crear fase
curl -X POST http://localhost:8080/api/fases -H "Content-Type: application/json" -d "{\"nombre\":\"Análisis\",\"descripcion\":\"Fase de análisis\",\"estadoId\":1}"

# 6. Crear sistema
curl -X POST http://localhost:8080/api/sistemas -H "Content-Type: application/json" -d "{\"nombre\":\"Sistema Core\",\"descripcion\":\"Sistema principal\",\"estadoId\":1}"

# 7. Crear subsistema
curl -X POST http://localhost:8080/api/subsistemas -H "Content-Type: application/json" -d "{\"nombre\":\"Módulo Usuarios\",\"descripcion\":\"Gestión usuarios\",\"sistemaId\":1,\"estadoId\":1}"

# 8. Crear rama
curl -X POST http://localhost:8080/api/ramas -H "Content-Type: application/json" -d "{\"nombre\":\"main\",\"descripcion\":\"Rama principal\"}"

# Listar todo
curl http://localhost:8080/api/estados
curl http://localhost:8080/api/empresas
curl http://localhost:8080/api/proyectos
```

## Manejo de errores

### Recurso no encontrado (404)
```bash
GET http://localhost:8080/api/empresas/999
```

**Respuesta:**
```json
{
  "timestamp": "2026-02-16T10:00:00",
  "status": 404,
  "error": "Not Found",
  "message": "Empresa con ID 999 no encontrado",
  "path": "/api/empresas/999"
}
```

### Error de relación (404)
```bash
POST http://localhost:8080/api/empresas
Content-Type: application/json

{
  "nombre": "Test",
  "estadoId": 999
}
```

**Respuesta:**
```json
{
  "timestamp": "2026-02-16T10:00:00",
  "status": 404,
  "error": "Not Found",
  "message": "Estado con ID 999 no encontrado",
  "path": "/api/empresas"
}
```

## Colección de Postman

Puedes importar esta colección en Postman:

1. Abre Postman
2. Click en "Import"
3. Copia y pega el JSON de la colección
4. Tendrás todos los endpoints listos para probar

## Verificar que el backend funciona

### 1. Health check simple
```bash
curl http://localhost:8080/api/estados
```

Si devuelve `[]` o una lista, el backend funciona correctamente.

### 2. Verificar CORS
Desde el navegador, abre la consola y ejecuta:

```javascript
fetch('http://localhost:8080/api/estados')
  .then(res => res.json())
  .then(data => console.log(data))
  .catch(err => console.error(err));
```

Si no hay errores de CORS, la configuración es correcta.

## Próximos pasos

1. Probar todos los endpoints
2. Verificar que los datos enriquecidos funcionan
3. Probar operaciones de actualización y eliminación
4. Conectar con el frontend React
5. Verificar que el frontend puede hacer CRUD completo

---

**Tip:** Guarda las respuestas de los POST para usar los IDs en las siguientes peticiones.
