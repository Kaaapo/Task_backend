package com.taskmanager.repository;

import com.taskmanager.model.Subsistema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository para Subsistema
 */
@Repository
public interface SubsistemaRepository extends JpaRepository<Subsistema, Long> {
    List<Subsistema> findBySistemaId(Long sistemaId);
    List<Subsistema> findByEstadoId(Long estadoId);
}
