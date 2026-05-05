# Task Manager — Backend

API REST del sistema de gestión de tareas y proyectos. Construida con **Spring Boot 3 + Spring Security + JPA/Hibernate + PostgreSQL**.

---

## Tecnologías

| Dependencia | Versión | Uso |
|-------------|---------|-----|
| Spring Boot | 3.2.2 | Framework principal |
| Spring Security | (incluido) | Autenticación y autorización |
| Spring Data JPA / Hibernate | (incluido) | ORM y acceso a datos |
| PostgreSQL Driver | (incluido) | Base de datos |
| JJWT | 0.12.6 | Generación y validación de JWT |
| Bucket4j | 8.10.1 | Rate limiting |
| Spring Validation | (incluido) | Validación de requests |
| Lombok | (incluido, opcional) | Reducción de boilerplate |
| Java | 17 | Versión del lenguaje |

---

## Requisitos previos

- Java 17+
- Maven 3.8+
- PostgreSQL 14+ corriendo localmente (o acceso a una BD remota)

---

## Configuración

### Variables de entorno

El backend toma toda su configuración desde variables de entorno (con valores por defecto para desarrollo local):

| Variable | Descripción | Default |
|----------|-------------|---------|
| `PORT` | Puerto del servidor | `8080` |
| `SPRING_PROFILES_ACTIVE` | Perfil activo (`default` o `prod`) | `default` |
| `DB_USERNAME` | Usuario de PostgreSQL | `postgres` |
| `DB_PASSWORD` | Contraseña de PostgreSQL | `postgres` |
| `JWT_SECRET` | Clave secreta para firmar JWT (mín. 256 bits) | Valor de desarrollo |
| `JWT_EXPIRATION` | Duración del access token en ms | `86400000` (24h) |
| `JWT_REFRESH_EXPIRATION` | Duración del refresh token en ms | `604800000` (7d) |
| `FRONTEND_URL` | URL del frontend (para CORS y emails) | `http://localhost:5173` |
| `CORS_ORIGINS` | Orígenes permitidos en CORS (separados por coma) | `http://localhost:5173` |
| `SENDGRID_API_KEY` | API Key de SendGrid para envío de emails | *(vacío — desactiva emails)* |
| `SENDGRID_FROM_EMAIL` | Dirección remitente verificada en SendGrid | *(vacío)* |
| `SENDGRID_FROM_NAME` | Nombre del remitente | `Task Manager` |

> En **Railway**, define estas variables en el panel del servicio bajo *Variables*.

### Base de datos local (desarrollo)

```sql
CREATE DATABASE taskmanager_db;
```

La URL por defecto es `jdbc:postgresql://localhost:5432/taskmanager_db`. Hibernate crea las tablas automáticamente al iniciar (`ddl-auto=update`).

---

## Ejecución

```bash
# Compilar y ejecutar
mvn spring-boot:run

# Solo compilar
mvn compile

# Build de producción (JAR)
mvn clean package -DskipTests

# Ejecutar el JAR
java -jar target/taskmanager-backend-0.0.1-SNAPSHOT.jar
```

El servidor queda disponible en `http://localhost:8080`.

---

## Datos iniciales

Para poblar los catálogos de **estados** y **tipos de proyecto** ejecuta el script `init.sql` una sola vez sobre la BD vacía:

```bash
psql -U postgres -d taskmanager_db -f init.sql
```

### Migración (BD existente)

Si ya tenías la BD con un esquema anterior, ejecuta `migration.sql` para aplicar los cambios de esquema sin perder datos:

```bash
psql -U postgres -d taskmanager_db -f migration.sql
```

El script elimina la columna `estado_id` de `tipos_proyecto` y agrega `deleted_at` a `tareas` y `proyectos` para el soft delete.

---

## Estructura del proyecto

```
src/main/java/com/taskmanager/
├── TaskmanagerApplication.java        # Punto de entrada
│
├── controller/                        # Endpoints REST
│   ├── AuthController.java
│   ├── EmpresaController.java
│   ├── EstadoController.java
│   ├── TipoProyectoController.java
│   ├── ProyectoController.java
│   ├── TareaController.java
│   ├── ComentarioTareaController.java
│   ├── MiembroEmpresaController.java
│   ├── MiembroProyectoController.java
│   └── UsuarioController.java
│
├── service/                           # Lógica de negocio
│   ├── I*Service.java                 # Interfaces de servicio (9)
│   ├── *Service.java                  # Implementaciones (implements I*Service)
│   ├── AuthService.java               # Autenticación, registro, reset password
│   ├── EmailService.java              # Envío de emails vía SendGrid API
│   ├── RefreshTokenService.java       # Gestión de refresh tokens
│   └── RegistroPersistenceService.java
│
├── model/                             # Entidades JPA
│   ├── Usuario.java
│   ├── Empresa.java
│   ├── Proyecto.java                  # Soft delete con @SQLDelete
│   ├── Tarea.java                     # Soft delete con @SQLDelete
│   ├── Estado.java
│   ├── TipoProyecto.java
│   ├── ComentarioTarea.java
│   ├── MiembroEmpresa.java
│   ├── MiembroProyecto.java
│   └── RefreshToken.java / TokenRecuperacion.java
│
├── dto/                               # Data Transfer Objects
├── repository/                        # Repositorios Spring Data JPA
│
├── security/                          # Configuración de seguridad
│   ├── SecurityConfig.java            # Cadena de filtros, CORS, rutas públicas
│   ├── JwtTokenProvider.java          # Generación y validación de JWT
│   ├── JwtAuthenticationFilter.java   # Filtro de autenticación por token
│   ├── CustomUserDetailsService.java  # Carga de usuario por email
│   └── RateLimitFilter.java           # Rate limiting con Bucket4j
│
└── exception/                         # Manejo de errores
    ├── GlobalExceptionHandler.java    # @ControllerAdvice centralizado
    ├── ResourceNotFoundException.java
    ├── CuentaBloqueadaException.java
    ├── EmailNoVerificadoException.java
    ├── RateLimitExceededException.java
    └── TokenExpiradoException.java
```

---

## Endpoints principales

### Autenticación (`/api/auth`)

| Método | Ruta | Descripción |
|--------|------|-------------|
| POST | `/api/auth/registro` | Crear cuenta (envía email de verificación) |
| POST | `/api/auth/login` | Iniciar sesión → devuelve `token` + `refreshToken` |
| POST | `/api/auth/refresh` | Renovar access token con refresh token |
| POST | `/api/auth/logout` | Invalidar refresh token |
| GET | `/api/auth/verificar-email` | Confirmar email con token |
| POST | `/api/auth/reenviar-verificacion` | Reenviar email de verificación |
| POST | `/api/auth/solicitar-reset` | Solicitar reset de contraseña |
| POST | `/api/auth/reset-password` | Cambiar contraseña con token |
| GET | `/api/auth/perfil` | Obtener perfil del usuario autenticado |

### Recursos protegidos (requieren `Authorization: Bearer <token>`)

| Recurso | Prefijo | Operaciones |
|---------|---------|-------------|
| Usuarios | `/api/usuarios` | `GET /me`, `PUT /me` |
| Empresas | `/api/empresas` | CRUD completo |
| Proyectos | `/api/proyectos` | CRUD + filtro por empresa |
| Tareas | `/api/tareas` | CRUD + filtro por proyecto/estado/asignado |
| Comentarios | `/api/tareas/{id}/comentarios` | CRUD |
| Estados | `/api/estados` | CRUD |
| Tipos de Proyecto | `/api/tipos-proyecto` | CRUD |
| Miembros Empresa | `/api/empresas/{id}/miembros` | Añadir/actualizar/eliminar |
| Miembros Proyecto | `/api/proyectos/{id}/miembros` | Añadir/actualizar/eliminar |

---

## Seguridad

- **JWT** — Access token (24h por defecto) + Refresh token (7d). El filtro `JwtAuthenticationFilter` valida el token en cada petición protegida.
- **Rate limiting** — `RateLimitFilter` limita las rutas de autenticación a 10 requests/min y el reset de contraseña a 3 requests/hora.
- **Bloqueo de cuenta** — Tras 5 intentos fallidos de login, la cuenta se bloquea 15 minutos.
- **Verificación de email** — El registro requiere confirmar el email antes de poder iniciar sesión.
- **CORS** — Configurado en `SecurityConfig` usando la variable `CORS_ORIGINS`.

---

## Patrones de diseño

- **Patrón Interface → Implementación** — Cada servicio de negocio define una interfaz `IXxxService` que la clase `XxxService` implementa. Los controladores inyectan solo la interfaz, desacoplando la capa de presentación de la lógica de negocio.
- **Soft Delete** — `Proyecto` y `Tarea` usan `@SQLDelete` y `@SQLRestriction` de Hibernate. El borrado físico se reemplaza por la asignación de `deleted_at`; los registros eliminados quedan ocultos automáticamente en todas las consultas.
- **DTOs** — Todas las respuestas y payloads de entrada usan DTOs, sin exponer entidades JPA directamente.
- **GlobalExceptionHandler** — Un `@ControllerAdvice` centraliza el manejo de excepciones y devuelve respuestas JSON consistentes con código HTTP apropiado.

---

## Perfiles

| Perfil | Activación | Diferencias |
|--------|------------|-------------|
| `default` | Local | `show-sql=true`, logs `DEBUG`, `ddl-auto=update` |
| `prod` | `SPRING_PROFILES_ACTIVE=prod` (Railway) | `show-sql=false`, logs `WARN/INFO`, `ddl-auto=update` |
