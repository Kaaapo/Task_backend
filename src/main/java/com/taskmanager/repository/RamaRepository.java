package com.taskmanager.repository;

import com.taskmanager.model.Rama;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository para Rama
 */
@Repository
public interface RamaRepository extends JpaRepository<Rama, Long> {
    // Métodos CRUD automáticos
}
