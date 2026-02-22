package com.taskmanager.repository;

import com.taskmanager.model.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository para Empresa
 */
@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, Long> {
    // Métodos personalizados
    Optional<Empresa> findByCorreo(String correo);
    List<Empresa> findByEstadoId(Long estadoId);
}
