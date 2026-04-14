# Task Manager Backend

API REST para gestión de proyectos desarrollada con Spring Boot y PostgreSQL. Desplegada en Railway.

## Tecnologías

- Java 17
- Spring Boot 3.2.2
- Spring Security + JWT
- Spring Data JPA
- PostgreSQL
- Maven

## Arquitectura

Arquitectura en capas (Layered Architecture):

```
Controller → Service → Repository → Database
```

### Estructura de paquetes

```
com.taskmanager
├── controller/     # REST API
├── service/        # Lógica de negocio
├── repository/     # Acceso a datos (JPA)
├── model/          # Entidades JPA
├── dto/            # Data Transfer Objects
├── exception/      # Manejo global de errores
├── config/         # CORS y configuraciones
└── security/       # JWT, filtros y Spring Security
```

## Requisitos previos

1. Java 17 o superior
2. Maven 3.6+
3. PostgreSQL 12+ (local o en Railway)

## Configuración local

### 1. Base de datos

Crear la base de datos en PostgreSQL (pgAdmin o terminal):

```sql
CREATE DATABASE taskmanager_db;
```

### 2. Configurar credenciales

Editar `src/main/resources/application.properties` o usar variables de entorno:

| Variable | Default | Descripción |
|----------|---------|-------------|
| `DATABASE_URL` | `jdbc:postgresql://localhost:5432/taskmanager_db` | URL de conexión |
| `DB_USERNAME` | `postgres` | Usuario BD |
| `DB_PASSWORD` | `postgres` | Contraseña BD |
| `JWT_SECRET` | (clave interna) | Secreto para firmar tokens |
| `JWT_EXPIRATION` | `86400000` (24h) | Tiempo de expiración JWT en ms |
| `CORS_ORIGINS` | `http://localhost:5173,http://localhost:3000` | Orígenes permitidos |
| `PORT` | `8080` | Puerto del servidor |

### 3. Compilar y ejecutar

```bash
mvn clean install
mvn spring-boot:run
```

La API estará en: `http://localhost:8080/api`

## Despliegue en Railway

### 1. Crear proyecto en Railway

1. Crear nuevo proyecto en [railway.app](https://railway.app)
2. Agregar servicio **PostgreSQL** (esto crea la BD automáticamente)
3. Agregar servicio desde **GitHub repo** (el backend)

### 2. Variables de entorno en Railway

Configurar en el servicio del backend:

```
DATABASE_URL=jdbc:postgresql://<host>:<port>/<db>   # Railway la provee automáticamente
DB_USERNAME=<usuario>                                 # Desde las credenciales de Railway PostgreSQL
DB_PASSWORD=<password>                                # Desde las credenciales de Railway PostgreSQL
JWT_SECRET=<clave-segura-de-256-bits>
# Orígenes del front separados por coma (Vercel + desarrollo local con VITE_API_URL directo)
CORS_ORIGINS=https://tu-frontend.vercel.app,http://localhost:5173
# Enlaces en correos (verificación, reset de contraseña)
FRONTEND_URL=https://tu-frontend.vercel.app
SPRING_PROFILES_ACTIVE=prod
```

Con el front usando `VITE_API_URL` apuntando a Railway, el navegador llama directo al API: incluye `http://localhost:5173` (y el dominio de producción del front) en `CORS_ORIGINS`.

### 3. Build

Railway detecta Maven automáticamente y ejecuta `mvn clean install`. Hibernate crea las tablas con `ddl-auto=update`.

## Endpoints

### Autenticación (públicos)
- `POST /api/auth/registro` - Registrar usuario
- `POST /api/auth/login` - Iniciar sesión
- `GET /api/auth/perfil` - Obtener perfil (requiere token)

### Usuarios (autenticados)
- `GET /api/usuarios/me` - Mi perfil
- `PUT /api/usuarios/me` - Actualizar mi perfil

### Empresas (autenticados)
- `GET /api/empresas` - Listar empresas
- `GET /api/empresas/{id}` - Obtener empresa
- `POST /api/empresas` - Crear empresa
- `PUT /api/empresas/{id}` - Actualizar empresa
- `DELETE /api/empresas/{id}` - Eliminar empresa

### Miembros de Empresa (autenticados)
- `GET /api/empresas/{id}/miembros` - Listar miembros
- `POST /api/empresas/{id}/miembros` - Agregar miembro
- `PUT /api/empresas/{id}/miembros/{mid}` - Cambiar rol
- `DELETE /api/empresas/{id}/miembros/{mid}` - Remover miembro

### Proyectos (autenticados)
- `GET /api/proyectos` - Listar proyectos
- `GET /api/proyectos/{id}` - Obtener proyecto
- `GET /api/proyectos/empresa/{empresaId}` - Por empresa
- `POST /api/proyectos` - Crear proyecto
- `PUT /api/proyectos/{id}` - Actualizar proyecto
- `DELETE /api/proyectos/{id}` - Eliminar proyecto

### Miembros de Proyecto (autenticados)
- `GET /api/proyectos/{id}/miembros` - Listar miembros
- `POST /api/proyectos/{id}/miembros` - Agregar miembro
- `PUT /api/proyectos/{id}/miembros/{mid}` - Cambiar rol
- `DELETE /api/proyectos/{id}/miembros/{mid}` - Remover miembro

### Tareas (autenticados)
- `GET /api/tareas` - Listar tareas
- `GET /api/tareas/{id}` - Obtener tarea
- `GET /api/tareas/proyecto/{proyectoId}` - Por proyecto
- `GET /api/tareas/asignado/{asignadoId}` - Por asignado
- `GET /api/tareas/proyecto/{pid}/estado/{eid}` - Por proyecto y estado
- `POST /api/tareas` - Crear tarea
- `PUT /api/tareas/{id}` - Actualizar tarea
- `DELETE /api/tareas/{id}` - Eliminar tarea

### Comentarios de Tarea (autenticados)
- `GET /api/tareas/{id}/comentarios` - Listar comentarios
- `POST /api/tareas/{id}/comentarios` - Crear comentario
- `PUT /api/tareas/{id}/comentarios/{cid}` - Editar comentario
- `DELETE /api/tareas/{id}/comentarios/{cid}` - Eliminar comentario

### Etiquetas (autenticados)
- `GET /api/etiquetas` - Listar etiquetas
- `GET /api/etiquetas/empresa/{empresaId}` - Por empresa
- `POST /api/etiquetas` - Crear etiqueta
- `PUT /api/etiquetas/{id}` - Actualizar etiqueta
- `DELETE /api/etiquetas/{id}` - Eliminar etiqueta

### Estados (públicos)
- `GET /api/estados` - Listar estados
- `GET /api/estados/{id}` - Obtener estado
- `POST /api/estados` - Crear estado
- `PUT /api/estados/{id}` - Actualizar estado
- `DELETE /api/estados/{id}` - Eliminar estado

### Tipos de Proyecto (públicos)
- `GET /api/tipos-proyecto` - Listar tipos
- `GET /api/tipos-proyecto/{id}` - Obtener tipo
- `POST /api/tipos-proyecto` - Crear tipo
- `PUT /api/tipos-proyecto/{id}` - Actualizar tipo
- `DELETE /api/tipos-proyecto/{id}` - Eliminar tipo

## Autenticación

Todos los endpoints autenticados requieren el header:

```
Authorization: Bearer <token-jwt>
```

El token se obtiene al registrarse o hacer login.

CORS está configurado para permitir peticiones desde:
- `http://localhost:5173` (Vite dev server)
- `http://localhost:3000` (alternativa)

## Testing

```bash
mvn test
```

## Build para producción

```bash
mvn clean package
java -jar target/taskmanager-backend-0.0.1-SNAPSHOT.jar
```

## Autor

Juan Camilo 🖤
