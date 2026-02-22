package com.taskmanager.repository;

import com.taskmanager.model.Sistema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository para Sistema
 */
@Repository
public interface SistemaRepository extends JpaRepository<Sistema, Long> {
    List<Sistema> findByEstadoId(Long estadoId);
}
