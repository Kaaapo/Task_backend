# Guía para separar el backend en su propio repositorio

Cuando estés listo para separar el backend del frontend y crear repositorios independientes, sigue estos pasos.

## Estado actual

```
Task_frontend/                    # Repositorio actual
├── src/                          # Frontend React
├── Task_backend/                 # Backend Spring Boot (temporal)
└── .gitignore                    # Ignora Task_backend/
```

## Estado final deseado

```
GitHub:
├── Task_frontend/                # Repositorio 1
│   └── src/                      # Solo frontend
│
└── Task_backend/                 # Repositorio 2 (nuevo)
    └── src/                      # Solo backend
```

## Pasos para separar

### 1. Preparar el backend

Asegúrate de que el backend funciona correctamente:

```bash
cd Task_backend
mvn clean install
mvn spring-boot:run
```

Verifica que:
- ✅ Compila sin errores
- ✅ Se conecta a PostgreSQL
- ✅ Los endpoints responden correctamente
- ✅ El frontend puede conectarse

### 2. Crear repositorio en GitHub

1. Ve a GitHub: https://github.com/new
2. Nombre del repositorio: `Task_backend`
3. Descripción: "Backend REST API para Task Manager con Spring Boot y PostgreSQL"
4. Visibilidad: Pública o Privada (según prefieras)
5. **NO** inicializar con README (ya tienes uno)
6. Click en "Create repository"

Copia la URL del repositorio:
```
https://github.com/tu-usuario/Task_backend.git
```

### 3. Mover la carpeta del backend

**Opción A: Mover fuera del frontend**

```bash
# Desde la carpeta Task_frontend
cd ..
move Task_frontend\Task_backend Task_backend
```

**Opción B: Copiar a nueva ubicación**

```bash
# Desde el escritorio
xcopy Task_frontend\Task_backend Task_backend /E /I
```

### 4. Inicializar Git en el backend

```bash
cd Task_backend

# Inicializar repositorio Git
git init

# Agregar todos los archivos
git add .

# Primer commit
git commit -m "Initial commit: Backend Spring Boot con arquitectura en capas"

# Conectar con GitHub
git remote add origin https://github.com/tu-usuario/Task_backend.git

# Subir al repositorio
git branch -M main
git push -u origin main
```

### 5. Limpiar el frontend

```bash
cd Task_frontend

# Si moviste la carpeta (Opción A), ya no existe Task_backend/
# Si copiaste (Opción B), elimínala:
rmdir /s Task_backend

# Actualizar .gitignore (quitar la línea Task_backend/)
# Editar .gitignore y eliminar:
# Task_backend/
```

### 6. Verificar ambos repositorios

**Backend:**
```bash
cd Task_backend
git remote -v
# Debería mostrar: origin  https://github.com/tu-usuario/Task_backend.git
```

**Frontend:**
```bash
cd Task_frontend
git status
# No debería mostrar Task_backend/ en los cambios
```

## Estructura final

### Repositorio Frontend (Task_frontend)

```
Task_frontend/
├── src/
│   ├── components/
│   ├── modules/
│   ├── shared/
│   │   └── config/
│   │       └── axiosConfig.js    # Apunta a localhost:8080/api
│   └── ...
├── package.json
└── README.md
```

### Repositorio Backend (Task_backend)

```
Task_backend/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/taskmanager/
│   │   │       ├── controller/
│   │   │       ├── service/
│   │   │       ├── repository/
│   │   │       ├── model/
│   │   │       └── ...
│   │   └── resources/
│   │       └── application.properties
│   └── test/
├── pom.xml
├── docker-compose.yml
└── README.md
```

## Clonar ambos proyectos (para nuevos desarrolladores)

```bash
# Clonar frontend
git clone https://github.com/tu-usuario/Task_frontend.git

# Clonar backend
git clone https://github.com/tu-usuario/Task_backend.git

# Estructura resultante:
# Escritorio/
# ├── Task_frontend/
# └── Task_backend/
```

## Ejecutar ambos proyectos

**Terminal 1 - Backend:**
```bash
cd Task_backend
docker-compose up -d        # PostgreSQL
mvn spring-boot:run         # Backend en puerto 8080
```

**Terminal 2 - Frontend:**
```bash
cd Task_frontend
npm install
npm run dev                 # Frontend en puerto 5173
```

## Actualizar README del frontend

Después de separar, actualiza el README del frontend para indicar:

```markdown
## Backend

Este frontend se conecta a un backend Spring Boot separado.

**Repositorio del backend:** https://github.com/tu-usuario/Task_backend

### Configuración

El frontend espera que el backend esté corriendo en:
- URL: `http://localhost:8080/api`
- Puerto: 8080

Para cambiar la URL del backend, edita `.env`:
```
VITE_API_URL=http://localhost:8080/api
```
```

## Actualizar README del backend

Actualiza el README del backend para indicar:

```markdown
## Frontend

Este backend sirve datos a un frontend React separado.

**Repositorio del frontend:** https://github.com/tu-usuario/Task_frontend

### CORS

El backend está configurado para permitir peticiones desde:
- `http://localhost:5173` (Vite dev server)
- `http://localhost:3000` (alternativa)

Para agregar más orígenes, edita `CorsConfig.java`.
```

## Ventajas de repositorios separados

✅ **Independencia:** Cada equipo trabaja en su repositorio  
✅ **Versionado:** Diferentes versiones y releases  
✅ **Despliegue:** Se despliegan independientemente  
✅ **CI/CD:** Pipelines separados  
✅ **Permisos:** Control de acceso diferente  
✅ **Claridad:** Cada repositorio tiene un propósito claro  

## Desventajas a considerar

⚠️ **Sincronización:** Cambios en la API requieren actualizar ambos  
⚠️ **Documentación:** Mantener documentación en ambos lugares  
⚠️ **Onboarding:** Nuevos desarrolladores deben clonar ambos  

## Alternativa: Monorepo

Si prefieres mantener todo junto, puedes usar un monorepo:

```
Task_Manager/
├── frontend/
│   └── (código React)
└── backend/
    └── (código Spring Boot)
```

Pero para este proyecto, **repositorios separados es la mejor opción**.

## Checklist final

Antes de separar, verifica:

- [ ] Backend compila sin errores
- [ ] Backend se conecta a PostgreSQL
- [ ] Todos los endpoints funcionan
- [ ] Frontend puede conectarse al backend
- [ ] CRUD completo funciona desde el frontend
- [ ] Tienes cuenta en GitHub
- [ ] Has creado el repositorio Task_backend en GitHub
- [ ] Has probado que todo funciona

## Comandos de resumen

```bash
# 1. Mover backend
cd ..
move Task_frontend\Task_backend Task_backend

# 2. Inicializar Git en backend
cd Task_backend
git init
git add .
git commit -m "Initial commit"
git remote add origin https://github.com/tu-usuario/Task_backend.git
git push -u origin main

# 3. Limpiar frontend
cd ..\Task_frontend
# Eliminar línea Task_backend/ del .gitignore
git add .gitignore
git commit -m "Remove backend from frontend repository"
git push
```

---

**¡Listo!** Ahora tienes dos repositorios independientes y profesionales.
