-- ===================================================
-- Script de migración — ejecutar UNA VEZ en BD existente
-- ===================================================

-- 1. Quitar estado_id de tipos_proyecto
--    (ya no es parte del modelo)
ALTER TABLE tipos_proyecto
    DROP CONSTRAINT IF EXISTS fk_tipos_proyecto_estado,
    DROP CONSTRAINT IF EXISTS tipos_proyecto_estado_id_fkey;

ALTER TABLE tipos_proyecto
    DROP COLUMN IF EXISTS estado_id;

-- 2. Agregar soft delete a tareas y proyectos
ALTER TABLE tareas
    ADD COLUMN IF NOT EXISTS deleted_at TIMESTAMP;

ALTER TABLE proyectos
    ADD COLUMN IF NOT EXISTS deleted_at TIMESTAMP;
