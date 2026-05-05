-- ===================================================
-- Script de limpieza de datos huérfanos
-- Ejecutar UNA VEZ para corregir registros anteriores
-- al fix de eliminación en cascada
-- ===================================================

-- 1. Eliminar comentarios de tareas que pertenecen a proyectos ya borrados
DELETE FROM comentarios_tarea
WHERE tarea_id IN (
    SELECT t.id FROM tareas t
    INNER JOIN proyectos p ON t.proyecto_id = p.id
    WHERE p.deleted_at IS NOT NULL AND t.deleted_at IS NULL
);

-- 2. Eliminar miembros de proyectos ya borrados
DELETE FROM miembros_proyecto
WHERE proyecto_id IN (
    SELECT id FROM proyectos WHERE deleted_at IS NOT NULL
);

-- 3. Marcar como eliminadas las tareas de proyectos ya borrados
UPDATE tareas
SET deleted_at = NOW()
WHERE proyecto_id IN (
    SELECT id FROM proyectos WHERE deleted_at IS NOT NULL
)
AND deleted_at IS NULL;

-- Verificación (opcional, ejecutar para confirmar):
-- SELECT COUNT(*) AS tareas_huerfanas
-- FROM tareas t
-- LEFT JOIN proyectos p ON t.proyecto_id = p.id
-- WHERE p.deleted_at IS NOT NULL AND t.deleted_at IS NULL;
