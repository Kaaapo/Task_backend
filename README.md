# Task Manager Backend

Backend REST API para Task Manager desarrollado con Spring Boot y PostgreSQL.

## Tecnologías

- **Java 17**
- **Spring Boot 3.2.2**
- **Spring Data JPA**
- **PostgreSQL**
- **Maven**

## Arquitectura

Arquitectura en capas (Layered Architecture):

```
Controller → Service → Repository → Database
```

### Estructura de paquetes

```
com.taskmanager
├── controller/     # Capa de presentación (REST API)
├── service/        # Capa de lógica de negocio
├── repository/     # Capa de acceso a datos
├── model/          # Entidades JPA
├── dto/            # Data Transfer Objects
├── exception/      # Manejo de excepciones
└── config/         # Configuraciones
```

## Requisitos previos

1. **Java 17** o superior
2. **Maven 3.6+**
3. **PostgreSQL 12+**
4. **Docker Desktop** (opcional, para BD en contenedor)

## Configuración de la base de datos

### Opción 1: PostgreSQL local

1. Instalar PostgreSQL
2. Crear la base de datos:

```sql
CREATE DATABASE taskmanager_db;
```

3. Configurar credenciales en `application.properties`

### Opción 2: PostgreSQL con Docker

```bash
docker run --name taskmanager_postgres \
  -e POSTGRES_DB=taskmanager_db \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=postgres \
  -p 5432:5432 \
  -d postgres:15-alpine
```

## Instalación y ejecución

### 1. Clonar el repositorio

```bash
git clone <repository-url>
cd Task_backend
```

### 2. Configurar application.properties

Editar `src/main/resources/application.properties` con tus credenciales de BD.

### 3. Compilar el proyecto

```bash
mvn clean install
```

### 4. Ejecutar la aplicación

```bash
mvn spring-boot:run
```

La API estará disponible en: `http://localhost:8080/api`

## Endpoints disponibles

### Estados
- `GET /api/estados` - Listar todos los estados
- `GET /api/estados/{id}` - Obtener estado por ID
- `POST /api/estados` - Crear estado
- `PUT /api/estados/{id}` - Actualizar estado
- `DELETE /api/estados/{id}` - Eliminar estado

### Empresas
- `GET /api/empresas` - Listar todas las empresas
- `GET /api/empresas/{id}` - Obtener empresa por ID
- `POST /api/empresas` - Crear empresa
- `PUT /api/empresas/{id}` - Actualizar empresa
- `DELETE /api/empresas/{id}` - Eliminar empresa

### Proyectos
- `GET /api/proyectos` - Listar todos los proyectos
- `GET /api/proyectos/{id}` - Obtener proyecto por ID
- `POST /api/proyectos` - Crear proyecto
- `PUT /api/proyectos/{id}` - Actualizar proyecto
- `DELETE /api/proyectos/{id}` - Eliminar proyecto

### Tipos de Proyecto
- `GET /api/tipos-proyecto` - Listar todos los tipos
- `GET /api/tipos-proyecto/{id}` - Obtener tipo por ID
- `POST /api/tipos-proyecto` - Crear tipo
- `PUT /api/tipos-proyecto/{id}` - Actualizar tipo
- `DELETE /api/tipos-proyecto/{id}` - Eliminar tipo

### Fases
- `GET /api/fases` - Listar todas las fases
- `GET /api/fases/{id}` - Obtener fase por ID
- `POST /api/fases` - Crear fase
- `PUT /api/fases/{id}` - Actualizar fase
- `DELETE /api/fases/{id}` - Eliminar fase

### Sistemas
- `GET /api/sistemas` - Listar todos los sistemas
- `GET /api/sistemas/{id}` - Obtener sistema por ID
- `POST /api/sistemas` - Crear sistema
- `PUT /api/sistemas/{id}` - Actualizar sistema
- `DELETE /api/sistemas/{id}` - Eliminar sistema

### Subsistemas
- `GET /api/subsistemas` - Listar todos los subsistemas
- `GET /api/subsistemas/{id}` - Obtener subsistema por ID
- `POST /api/subsistemas` - Crear subsistema
- `PUT /api/subsistemas/{id}` - Actualizar subsistema
- `DELETE /api/subsistemas/{id}` - Eliminar subsistema

### Ramas
- `GET /api/ramas` - Listar todas las ramas
- `GET /api/ramas/{id}` - Obtener rama por ID
- `POST /api/ramas` - Crear rama
- `PUT /api/ramas/{id}` - Actualizar rama
- `DELETE /api/ramas/{id}` - Eliminar rama

## Conexión con el Frontend

El frontend React se conecta a través de Axios:

```javascript
// axiosConfig.js
const API_URL = 'http://localhost:8080/api';
```

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

Task Manager Team
