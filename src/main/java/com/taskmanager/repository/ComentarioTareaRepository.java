package com.taskmanager.repository;

import com.taskmanager.model.ComentarioTarea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComentarioTareaRepository extends JpaRepository<ComentarioTarea, Long> {
    List<ComentarioTarea> findByTareaIdOrderByFechaCreacionDesc(Long tareaId);
}
