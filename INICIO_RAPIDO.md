# 🚀 Inicio Rápido - Task Manager Backend

## ¡Bienvenido!

Has creado exitosamente un backend Spring Boot completo con arquitectura en capas.

## 📊 Estadísticas del proyecto

- **Archivos creados:** 52+
- **Entidades:** 8 (Estado, Empresa, TipoProyecto, Proyecto, Fase, Sistema, Subsistema, Rama)
- **Endpoints REST:** 40+ (CRUD completo para cada entidad)
- **Líneas de código:** ~3000+
- **Arquitectura:** Capas (Controller → Service → Repository)

## ⚡ Inicio en 3 pasos

### 1️⃣ Iniciar PostgreSQL

```bash
cd Task_backend
docker-compose up -d
```

Esto crea:
- ✅ PostgreSQL en puerto 5432
- ✅ Base de datos `taskmanager_db`
- ✅ Usuario: `postgres` / Password: `postgres`

### 2️⃣ Compilar el proyecto

```bash
mvn clean install
```

### 3️⃣ Ejecutar el backend

```bash
mvn spring-boot:run
```

**¡Listo!** El backend está corriendo en `http://localhost:8080/api`

## 🧪 Probar que funciona

Abre una nueva terminal y ejecuta:

```bash
curl http://localhost:8080/api/estados
```

Si ves `[]` o una lista, **¡funciona correctamente!** 🎉

## 📝 Crear datos de prueba

```bash
# Crear un estado
curl -X POST http://localhost:8080/api/estados \
  -H "Content-Type: application/json" \
  -d "{\"nombre\":\"Activo\",\"descripcion\":\"Estado activo\"}"

# Crear una empresa
curl -X POST http://localhost:8080/api/empresas \
  -H "Content-Type: application/json" \
  -d "{\"nombre\":\"TechCorp\",\"descripcion\":\"Empresa tech\",\"correo\":\"info@techcorp.com\",\"estadoId\":1}"

# Ver empresas
curl http://localhost:8080/api/empresas
```

## 🔗 Conectar con el frontend

El frontend ya está configurado para conectarse automáticamente.

**Terminal 1 - Backend:**
```bash
cd Task_backend
mvn spring-boot:run
```

**Terminal 2 - Frontend:**
```bash
cd Task_frontend
npm run dev
```

Abre el navegador en `http://localhost:5173` y verás el frontend conectado al backend.

## 📚 Documentación disponible

| Archivo | Descripción |
|---------|-------------|
| `README.md` | Documentación general |
| `INSTRUCCIONES.md` | Guía de instalación detallada |
| `PRUEBAS_API.md` | Ejemplos de todos los endpoints |
| `RESUMEN_PROYECTO.md` | Resumen completo del proyecto |
| `SEPARAR_REPOSITORIO.md` | Cómo separar en repositorio propio |
| `INICIO_RAPIDO.md` | Esta guía |

## 🎯 Endpoints disponibles

| Entidad | Base URL | Métodos |
|---------|----------|---------|
| Estados | `/api/estados` | GET, POST, PUT, DELETE |
| Empresas | `/api/empresas` | GET, POST, PUT, DELETE |
| Tipos Proyecto | `/api/tipos-proyecto` | GET, POST, PUT, DELETE |
| Proyectos | `/api/proyectos` | GET, POST, PUT, DELETE |
| Fases | `/api/fases` | GET, POST, PUT, DELETE |
| Sistemas | `/api/sistemas` | GET, POST, PUT, DELETE |
| Subsistemas | `/api/subsistemas` | GET, POST, PUT, DELETE |
| Ramas | `/api/ramas` | GET, POST, PUT, DELETE |

## 🛠️ Comandos útiles

```bash
# Compilar sin tests
mvn clean install -DskipTests

# Ver logs de PostgreSQL
docker-compose logs -f postgres

# Detener PostgreSQL
docker-compose down

# Reiniciar backend (Ctrl+C y luego)
mvn spring-boot:run
```

## ❓ Solución de problemas

### El backend no inicia

1. Verifica que PostgreSQL esté corriendo:
   ```bash
   docker ps
   ```

2. Verifica que el puerto 8080 esté libre:
   ```bash
   netstat -an | findstr 8080
   ```

### No se conecta a la base de datos

Verifica las credenciales en `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/taskmanager_db
spring.datasource.username=postgres
spring.datasource.password=postgres
```

### El frontend no se conecta

1. Verifica que el backend esté en puerto 8080
2. Verifica CORS en `CorsConfig.java`
3. Verifica la URL en el frontend: `.env`

## 🎓 Próximos pasos

1. ✅ Probar todos los endpoints con Postman
2. ✅ Conectar con el frontend React
3. ✅ Crear datos de prueba
4. ⬜ Agregar validaciones con `@Valid`
5. ⬜ Implementar tests unitarios
6. ⬜ Separar en repositorio propio

## 💡 Tips

- Usa **Postman** para probar los endpoints
- Revisa los logs en la consola para debug
- Los DTOs incluyen datos enriquecidos (nombres de relaciones)
- CORS ya está configurado para el frontend
- Hibernate crea las tablas automáticamente (`ddl-auto=update`)

## 🌟 Características implementadas

✅ CRUD completo para 8 entidades  
✅ DTOs con datos enriquecidos  
✅ Manejo de excepciones centralizado  
✅ CORS configurado  
✅ Validación de relaciones  
✅ Transacciones con `@Transactional`  
✅ Respuestas HTTP correctas  
✅ Docker Compose para PostgreSQL  
✅ Arquitectura en capas limpia  
✅ Código documentado  

## 📞 Recursos adicionales

- **Spring Boot Docs:** https://spring.io/projects/spring-boot
- **PostgreSQL Docs:** https://www.postgresql.org/docs/
- **Postman:** https://www.postman.com/
- **Docker:** https://www.docker.com/

---

## 🎉 ¡Felicidades!

Has creado un backend profesional con Spring Boot.

**Siguiente paso:** Ejecuta `mvn spring-boot:run` y empieza a desarrollar.

---

**Creado con arquitectura en capas para Task Manager**  
**Tecnologías:** Java 17 • Spring Boot 3.2.2 • PostgreSQL • Maven • Docker
