package com.taskmanager.repository;

import com.taskmanager.model.Tarea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TareaRepository extends JpaRepository<Tarea, Long> {
    List<Tarea> findByProyectoId(Long proyectoId);
    List<Tarea> findByAsignadoId(Long asignadoId);
    List<Tarea> findByProyectoIdAndEstadoId(Long proyectoId, Long estadoId);
    List<Tarea> findByProyectoIdOrderByOrdenAsc(Long proyectoId);

    @Modifying
    @Query(value = "UPDATE tareas SET deleted_at = NOW() WHERE proyecto_id = :proyectoId AND deleted_at IS NULL", nativeQuery = true)
    void softDeleteByProyectoId(@Param("proyectoId") Long proyectoId);

    @Query(value = "SELECT id FROM tareas WHERE proyecto_id = :proyectoId AND deleted_at IS NULL", nativeQuery = true)
    List<Long> findIdsByProyectoId(@Param("proyectoId") Long proyectoId);
}
