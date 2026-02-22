# Instrucciones de instalación y ejecución

## Requisitos previos

1. **Java 17** instalado
2. **Maven** instalado
3. **PostgreSQL** (local o Docker)
4. **Docker Desktop** (opcional, para BD en contenedor)

## Opción 1: Ejecutar con PostgreSQL local

### 1. Instalar PostgreSQL

Descargar e instalar PostgreSQL desde: https://www.postgresql.org/download/

### 2. Crear la base de datos

```sql
CREATE DATABASE taskmanager_db;
```

### 3. Configurar credenciales

Editar `src/main/resources/application.properties`:

```properties
spring.datasource.username=tu_usuario
spring.datasource.password=tu_password
```

### 4. Compilar el proyecto

```bash
mvn clean install
```

### 5. Ejecutar la aplicación

```bash
mvn spring-boot:run
```

La API estará disponible en: `http://localhost:8080/api`

## Opción 2: Ejecutar con Docker (Recomendado)

### 1. Iniciar PostgreSQL con Docker Compose

```bash
docker-compose up -d
```

Esto creará:
- Contenedor PostgreSQL en puerto 5432
- Base de datos `taskmanager_db`
- Usuario: `postgres`
- Password: `postgres`

### 2. Verificar que PostgreSQL está corriendo

```bash
docker ps
```

Deberías ver el contenedor `taskmanager_postgres` corriendo.

### 3. Compilar el proyecto

```bash
mvn clean install
```

### 4. Ejecutar la aplicación

```bash
mvn spring-boot:run
```

## Verificar que funciona

### 1. Probar endpoint de estados

```bash
curl http://localhost:8080/api/estados
```

### 2. Crear un estado de prueba

```bash
curl -X POST http://localhost:8080/api/estados \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Prueba","descripcion":"Estado de prueba"}'
```

## Conectar con el frontend

El frontend React ya está configurado para conectarse a `http://localhost:8080/api`

1. Asegúrate de que el backend esté corriendo en puerto 8080
2. Inicia el frontend: `npm run dev`
3. El frontend debería conectarse automáticamente

## Comandos útiles

### Maven

```bash
# Compilar sin tests
mvn clean install -DskipTests

# Solo compilar
mvn clean compile

# Ejecutar tests
mvn test

# Limpiar proyecto
mvn clean
```

### Docker

```bash
# Iniciar PostgreSQL
docker-compose up -d

# Ver logs de PostgreSQL
docker-compose logs -f postgres

# Detener PostgreSQL
docker-compose down

# Detener y eliminar datos (¡cuidado!)
docker-compose down -v

# Conectarse a PostgreSQL
docker exec -it taskmanager_postgres psql -U postgres -d taskmanager_db
```

## Solución de problemas

### Error: Puerto 8080 ocupado

Cambiar el puerto en `application.properties`:

```properties
server.port=8081
```

Y actualizar el frontend en `.env`:

```
VITE_API_URL=http://localhost:8081/api
```

### Error: No se puede conectar a PostgreSQL

1. Verificar que PostgreSQL está corriendo:
   ```bash
   docker ps
   ```

2. Verificar credenciales en `application.properties`

3. Verificar puerto 5432:
   ```bash
   netstat -an | findstr 5432
   ```

### Error: Lombok no funciona

Si tu IDE no reconoce Lombok:

1. **IntelliJ IDEA**: Instalar plugin Lombok
2. **Eclipse**: Descargar lombok.jar y ejecutar
3. **VS Code**: Instalar extensión "Lombok Annotations Support"

## Estructura del proyecto

```
Task_backend/
├── src/main/java/com/taskmanager/
│   ├── controller/      # REST Controllers
│   ├── service/         # Lógica de negocio
│   ├── repository/      # Acceso a datos
│   ├── model/           # Entidades JPA
│   ├── dto/             # Data Transfer Objects
│   ├── exception/       # Manejo de errores
│   └── config/          # Configuraciones
├── src/main/resources/
│   └── application.properties
├── pom.xml
└── docker-compose.yml
```

## Próximos pasos

1. Probar todos los endpoints con Postman o curl
2. Conectar con el frontend React
3. Agregar datos de prueba
4. Implementar validaciones adicionales
5. Agregar tests unitarios
