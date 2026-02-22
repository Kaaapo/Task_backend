package com.taskmanager.repository;

import com.taskmanager.model.TipoProyecto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository para TipoProyecto
 */
@Repository
public interface TipoProyectoRepository extends JpaRepository<TipoProyecto, Long> {
    List<TipoProyecto> findByEstadoId(Long estadoId);
}
