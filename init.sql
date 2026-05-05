-- ===================================
-- Datos iniciales para el Gestor de Proyectos
-- Ejecutar una sola vez sobre la BD vacía
-- ===================================

-- Estados del sistema
INSERT INTO estados (nombre, descripcion, color) VALUES
    ('Activo', 'Elemento activo y operativo', '#22c55e'),
    ('Inactivo', 'Elemento inactivo o deshabilitado', '#6b7280'),
    ('En Proceso', 'Elemento en desarrollo o progreso', '#3b82f6'),
    ('Completado', 'Elemento finalizado exitosamente', '#10b981'),
    ('Pausado', 'Elemento temporalmente detenido', '#f59e0b'),
    ('Cancelado', 'Elemento cancelado permanentemente', '#ef4444'),
    ('Pendiente', 'Elemento pendiente de iniciar', '#8b5cf6'),
    ('En Revisión', 'Elemento en proceso de revisión', '#06b6d4');

-- Tipos de proyecto
INSERT INTO tipos_proyecto (nombre, descripcion, color) VALUES
    ('Software', 'Desarrollo de aplicaciones y sistemas', '#3b82f6'),
    ('Web', 'Desarrollo de sitios y aplicaciones web', '#8b5cf6'),
    ('Móvil', 'Desarrollo de aplicaciones móviles', '#10b981'),
    ('Infraestructura', 'Proyectos de infraestructura tecnológica', '#f59e0b'),
    ('Diseño', 'Proyectos de diseño gráfico y UX/UI', '#ec4899'),
    ('Marketing', 'Campañas y estrategias de marketing', '#14b8a6'),
    ('Consultoría', 'Proyectos de consultoría y asesoría', '#6366f1'),
    ('Investigación', 'Proyectos de investigación y desarrollo', '#f97316');
