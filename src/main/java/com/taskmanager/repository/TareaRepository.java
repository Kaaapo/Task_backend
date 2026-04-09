package com.taskmanager.repository;

import com.taskmanager.model.Tarea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TareaRepository extends JpaRepository<Tarea, Long> {
    List<Tarea> findByProyectoId(Long proyectoId);
    List<Tarea> findByAsignadoId(Long asignadoId);
    List<Tarea> findByProyectoIdAndEstadoId(Long proyectoId, Long estadoId);
    List<Tarea> findByProyectoIdOrderByOrdenAsc(Long proyectoId);
}
