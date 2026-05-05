package com.taskmanager.repository;

import com.taskmanager.model.ComentarioTarea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComentarioTareaRepository extends JpaRepository<ComentarioTarea, Long> {
    List<ComentarioTarea> findByTareaIdOrderByFechaCreacionDesc(Long tareaId);

    @Modifying
    @Query(value = "DELETE FROM comentarios_tarea WHERE tarea_id = :tareaId", nativeQuery = true)
    void deleteByTareaId(@Param("tareaId") Long tareaId);

    @Modifying
    @Query(value = "DELETE FROM comentarios_tarea WHERE tarea_id IN (SELECT id FROM tareas WHERE proyecto_id = :proyectoId)", nativeQuery = true)
    void deleteByProyectoId(@Param("proyectoId") Long proyectoId);
}
