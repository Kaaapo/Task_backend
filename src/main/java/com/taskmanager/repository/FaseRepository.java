package com.taskmanager.repository;

import com.taskmanager.model.Fase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository para Fase
 */
@Repository
public interface FaseRepository extends JpaRepository<Fase, Long> {
    List<Fase> findByEstadoId(Long estadoId);
}
