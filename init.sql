-- Script de inicialización de la base de datos
-- Se ejecuta automáticamente al crear el contenedor de PostgreSQL

-- Crear tabla estados si no existe
-- (necesaria antes de que Spring Boot arranque con ddl-auto=update)
CREATE TABLE IF NOT EXISTS estados (
    id          BIGSERIAL PRIMARY KEY,
    nombre      VARCHAR(100) NOT NULL,
    descripcion VARCHAR(500)
);

-- Insertar datos iniciales de Estados
INSERT INTO estados (nombre, descripcion) VALUES
('Activo',      'Estado activo'),
('Inactivo',    'Estado inactivo'),
('En Proceso',  'En proceso de desarrollo'),
('Completado',  'Proyecto completado'),
('Pausado',     'Temporalmente pausado')
ON CONFLICT DO NOTHING;
