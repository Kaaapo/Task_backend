-- Script de inicialización de la base de datos
-- Se ejecuta automáticamente al crear el contenedor de PostgreSQL

-- Crear la base de datos si no existe
-- (Ya se crea automáticamente por POSTGRES_DB en docker-compose)

-- Insertar datos iniciales de Estados
INSERT INTO estados (nombre, descripcion) VALUES
('Activo', 'Estado activo'),
('Inactivo', 'Estado inactivo'),
('En Proceso', 'En proceso de desarrollo'),
('Completado', 'Proyecto completado'),
('Pausado', 'Temporalmente pausado')
ON CONFLICT DO NOTHING;

-- Nota: Los demás datos se insertarán a través de la API
-- o se pueden agregar aquí si se desean datos de prueba iniciales
