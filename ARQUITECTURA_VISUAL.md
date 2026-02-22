# Arquitectura Visual del Backend

## 🏗️ Arquitectura en Capas

```
┌─────────────────────────────────────────────────────────────────┐
│                         FRONTEND REACT                          │
│                    http://localhost:5173                        │
│                                                                 │
│  src/shared/config/axiosConfig.js                              │
│  baseURL: http://localhost:8080/api                            │
└────────────────────────┬────────────────────────────────────────┘
                         │
                         │ HTTP REST API
                         │ (JSON)
                         ▼
┌─────────────────────────────────────────────────────────────────┐
│                    SPRING BOOT BACKEND                          │
│                    http://localhost:8080                        │
│                                                                 │
│  ┌───────────────────────────────────────────────────────────┐ │
│  │              CONTROLLER LAYER                             │ │
│  │              @RestController                              │ │
│  │  ┌──────────────────────────────────────────────────┐    │ │
│  │  │ EstadoController     │ EmpresaController         │    │ │
│  │  │ TipoProyectoController│ ProyectoController       │    │ │
│  │  │ FaseController       │ SistemaController         │    │ │
│  │  │ SubsistemaController │ RamaController            │    │ │
│  │  └──────────────────────────────────────────────────┘    │ │
│  │                                                           │ │
│  │  Responsabilidades:                                       │ │
│  │  • Recibir peticiones HTTP                               │ │
│  │  • Validar entrada                                        │ │
│  │  • Retornar respuestas JSON                              │ │
│  │  • Códigos HTTP (200, 201, 204, 404, 500)               │ │
│  └───────────────────┬───────────────────────────────────────┘ │
│                      │                                          │
│  ┌───────────────────▼───────────────────────────────────────┐ │
│  │              SERVICE LAYER                                │ │
│  │              @Service @Transactional                      │ │
│  │  ┌──────────────────────────────────────────────────┐    │ │
│  │  │ EstadoService        │ EmpresaService            │    │ │
│  │  │ TipoProyectoService  │ ProyectoService           │    │ │
│  │  │ FaseService          │ SistemaService            │    │ │
│  │  │ SubsistemaService    │ RamaService               │    │ │
│  │  └──────────────────────────────────────────────────┘    │ │
│  │                                                           │ │
│  │  Responsabilidades:                                       │ │
│  │  • Lógica de negocio                                     │ │
│  │  • Validaciones de negocio                               │ │
│  │  • Transformaciones Entity ↔ DTO                         │ │
│  │  • Enriquecimiento de datos                              │ │
│  │  • Gestión de transacciones                              │ │
│  └───────────────────┬───────────────────────────────────────┘ │
│                      │                                          │
│  ┌───────────────────▼───────────────────────────────────────┐ │
│  │              REPOSITORY LAYER                             │ │
│  │              @Repository extends JpaRepository            │ │
│  │  ┌──────────────────────────────────────────────────┐    │ │
│  │  │ EstadoRepository     │ EmpresaRepository         │    │ │
│  │  │ TipoProyectoRepository│ ProyectoRepository       │    │ │
│  │  │ FaseRepository       │ SistemaRepository         │    │ │
│  │  │ SubsistemaRepository │ RamaRepository            │    │ │
│  │  └──────────────────────────────────────────────────┘    │ │
│  │                                                           │ │
│  │  Responsabilidades:                                       │ │
│  │  • Acceso a base de datos                                │ │
│  │  • Queries SQL/JPQL                                      │ │
│  │  • CRUD automático (JpaRepository)                       │ │
│  │  • Métodos personalizados                                │ │
│  └───────────────────┬───────────────────────────────────────┘ │
│                      │                                          │
│  ┌───────────────────▼───────────────────────────────────────┐ │
│  │              MODEL LAYER (Entities)                       │ │
│  │              @Entity @Table                               │ │
│  │  ┌──────────────────────────────────────────────────┐    │ │
│  │  │ Estado    │ Empresa    │ TipoProyecto            │    │ │
│  │  │ Proyecto  │ Fase       │ Sistema                 │    │ │
│  │  │ Subsistema│ Rama                                  │    │ │
│  │  └──────────────────────────────────────────────────┘    │ │
│  │                                                           │ │
│  │  Responsabilidades:                                       │ │
│  │  • Mapeo de tablas (ORM)                                 │ │
│  │  • Relaciones entre entidades                            │ │
│  │  • Constraints y validaciones                            │ │
│  └───────────────────┬───────────────────────────────────────┘ │
│                      │                                          │
│  ┌───────────────────▼───────────────────────────────────────┐ │
│  │              CROSS-CUTTING CONCERNS                       │ │
│  │  ┌──────────────────────────────────────────────────┐    │ │
│  │  │ • CorsConfig (CORS)                              │    │ │
│  │  │ • GlobalExceptionHandler (Errores)               │    │ │
│  │  │ • DTOs (Transferencia de datos)                  │    │ │
│  │  └──────────────────────────────────────────────────┘    │ │
│  └───────────────────────────────────────────────────────────┘ │
└────────────────────────┬────────────────────────────────────────┘
                         │
                         │ JDBC
                         │
                         ▼
┌─────────────────────────────────────────────────────────────────┐
│                    POSTGRESQL DATABASE                          │
│                    localhost:5432                               │
│                    taskmanager_db                               │
│                                                                 │
│  Tablas:                                                        │
│  • estados                                                      │
│  • empresas                                                     │
│  • tipos_proyecto                                               │
│  • proyectos                                                    │
│  • fases                                                        │
│  • sistemas                                                     │
│  • subsistemas                                                  │
│  • ramas                                                        │
└─────────────────────────────────────────────────────────────────┘
```

## 📊 Flujo de una petición

### Ejemplo: Crear una Empresa

```
1. FRONTEND
   ↓
   POST http://localhost:8080/api/empresas
   Body: {
     "nombre": "TechCorp",
     "descripcion": "Empresa tech",
     "correo": "info@techcorp.com",
     "estadoId": 1
   }
   
2. CONTROLLER (EmpresaController)
   ↓
   @PostMapping
   public ResponseEntity<EmpresaDTO> create(@RequestBody EmpresaDTO dto)
   
3. SERVICE (EmpresaService)
   ↓
   • Validar que estadoId existe
   • Crear entidad Empresa
   • Asignar relaciones
   
4. REPOSITORY (EmpresaRepository)
   ↓
   • save(empresa)
   • JPA genera SQL INSERT
   
5. DATABASE (PostgreSQL)
   ↓
   INSERT INTO empresas (nombre, descripcion, correo, estado_id)
   VALUES ('TechCorp', 'Empresa tech', 'info@techcorp.com', 1)
   
6. RESPONSE
   ↓
   {
     "id": 1,
     "nombre": "TechCorp",
     "descripcion": "Empresa tech",
     "correo": "info@techcorp.com",
     "estadoId": 1,
     "estadoNombre": "Activo"  ← Enriquecido
   }
   
7. FRONTEND
   ↓
   Recibe el objeto y actualiza la UI
```

## 🔄 Relaciones entre entidades

```
┌──────────┐
│  Estado  │
└────┬─────┘
     │ 1
     │
     │ N
     ├─────────────┬──────────────┬──────────────┬──────────────┐
     │             │              │              │              │
┌────▼─────┐  ┌───▼────┐    ┌───▼────┐    ┌───▼────┐    ┌───▼────┐
│ Empresa  │  │  Tipo  │    │  Fase  │    │Sistema │    │  ...   │
└────┬─────┘  │Proyecto│    └────────┘    └───┬────┘    └────────┘
     │        └───┬────┘                       │
     │ N          │ N                          │ 1
     │            │                            │
     │            │                            │ N
     │            │                       ┌────▼──────┐
     │            │                       │Subsistema │
     │            │                       └───────────┘
     │ 1          │ 1
     └────────┬───┘
              │ N
         ┌────▼─────┐
         │ Proyecto │
         └──────────┘

┌──────────┐
│   Rama   │  (Independiente)
└──────────┘
```

## 📦 Estructura de paquetes

```
com.taskmanager
│
├── 📱 controller/           # REST API Endpoints
│   ├── EstadoController
│   ├── EmpresaController
│   ├── TipoProyectoController
│   ├── ProyectoController
│   ├── FaseController
│   ├── SistemaController
│   ├── SubsistemaController
│   └── RamaController
│
├── 💼 service/              # Lógica de negocio
│   ├── EstadoService
│   ├── EmpresaService
│   ├── TipoProyectoService
│   ├── ProyectoService
│   ├── FaseService
│   ├── SistemaService
│   ├── SubsistemaService
│   └── RamaService
│
├── 🗄️ repository/          # Acceso a datos
│   ├── EstadoRepository
│   ├── EmpresaRepository
│   ├── TipoProyectoRepository
│   ├── ProyectoRepository
│   ├── FaseRepository
│   ├── SistemaRepository
│   ├── SubsistemaRepository
│   └── RamaRepository
│
├── 🏛️ model/               # Entidades JPA
│   ├── Estado
│   ├── Empresa
│   ├── TipoProyecto
│   ├── Proyecto
│   ├── Fase
│   ├── Sistema
│   ├── Subsistema
│   └── Rama
│
├── 📋 dto/                 # Data Transfer Objects
│   ├── EstadoDTO
│   ├── EmpresaDTO
│   ├── TipoProyectoDTO
│   ├── ProyectoDTO
│   ├── FaseDTO
│   ├── SistemaDTO
│   ├── SubsistemaDTO
│   └── RamaDTO
│
├── ⚠️ exception/           # Manejo de errores
│   ├── ResourceNotFoundException
│   └── GlobalExceptionHandler
│
└── ⚙️ config/              # Configuraciones
    └── CorsConfig
```

## 🔐 Principios SOLID aplicados

### Single Responsibility Principle (SRP)
- Cada clase tiene una única responsabilidad
- Controller: manejo HTTP
- Service: lógica de negocio
- Repository: acceso a datos

### Open/Closed Principle (OCP)
- Abierto para extensión (agregar nuevos servicios)
- Cerrado para modificación (no cambiar código existente)

### Liskov Substitution Principle (LSP)
- Interfaces Repository son intercambiables
- JpaRepository proporciona contrato estándar

### Interface Segregation Principle (ISP)
- Repositorios específicos por entidad
- No interfaces grandes y monolíticas

### Dependency Inversion Principle (DIP)
- Dependencias mediante interfaces
- Inyección de dependencias con @Autowired
- Capas superiores no dependen de implementaciones

## 🎯 Patrones de diseño implementados

### Repository Pattern
- Abstracción del acceso a datos
- JpaRepository proporciona CRUD automático

### DTO Pattern
- Separación entre entidades y objetos de transferencia
- Enriquecimiento de datos sin modificar entidades

### Service Layer Pattern
- Lógica de negocio centralizada
- Reutilizable desde diferentes controllers

### Exception Handler Pattern
- Manejo centralizado de excepciones
- Respuestas consistentes de error

## 🚀 Ventajas de esta arquitectura

✅ **Mantenibilidad:** Código organizado y fácil de entender  
✅ **Escalabilidad:** Fácil agregar nuevas funcionalidades  
✅ **Testabilidad:** Cada capa se prueba independientemente  
✅ **Reutilización:** Servicios reutilizables  
✅ **Separación de responsabilidades:** Cada capa tiene un propósito claro  
✅ **Flexibilidad:** Cambiar una capa sin afectar las demás  

---

**Arquitectura en capas para Task Manager Backend**
