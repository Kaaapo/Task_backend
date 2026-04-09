-- ===================================
-- Datos iniciales para el Gestor de Proyectos
-- ===================================

-- Estados del sistema
INSERT INTO estados (nombre, descripcion, color, orden) VALUES
    ('Activo', 'Elemento activo y operativo', '#22c55e', 1),
    ('Inactivo', 'Elemento inactivo o deshabilitado', '#6b7280', 2),
    ('En Proceso', 'Elemento en desarrollo o progreso', '#3b82f6', 3),
    ('Completado', 'Elemento finalizado exitosamente', '#10b981', 4),
    ('Pausado', 'Elemento temporalmente detenido', '#f59e0b', 5),
    ('Cancelado', 'Elemento cancelado permanentemente', '#ef4444', 6),
    ('Pendiente', 'Elemento pendiente de iniciar', '#8b5cf6', 7),
    ('En Revisión', 'Elemento en proceso de revisión', '#06b6d4', 8)
ON CONFLICT DO NOTHING;

-- Tipos de proyecto
INSERT INTO tipos_proyecto (nombre, descripcion, color, icono, estado_id) VALUES
    ('Software', 'Desarrollo de aplicaciones y sistemas', '#3b82f6', 'code', 1),
    ('Web', 'Desarrollo de sitios y aplicaciones web', '#8b5cf6', 'globe', 1),
    ('Móvil', 'Desarrollo de aplicaciones móviles', '#10b981', 'smartphone', 1),
    ('Infraestructura', 'Proyectos de infraestructura tecnológica', '#f59e0b', 'server', 1),
    ('Diseño', 'Proyectos de diseño gráfico y UX/UI', '#ec4899', 'palette', 1),
    ('Marketing', 'Campañas y estrategias de marketing', '#14b8a6', 'megaphone', 1),
    ('Consultoría', 'Proyectos de consultoría y asesoría', '#6366f1', 'briefcase', 1),
    ('Investigación', 'Proyectos de investigación y desarrollo', '#f97316', 'search', 1)
ON CONFLICT DO NOTHING;
