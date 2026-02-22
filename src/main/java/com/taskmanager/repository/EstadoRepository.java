package com.taskmanager.repository;

import com.taskmanager.model.Estado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository para Estado
 * Proporciona operaciones CRUD automáticas
 */
@Repository
public interface EstadoRepository extends JpaRepository<Estado, Long> {
    // Spring Data JPA genera automáticamente los métodos CRUD
    // Métodos personalizados opcionales se pueden agregar aquí
}
