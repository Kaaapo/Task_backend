package com.taskmanager.repository;

import com.taskmanager.model.Proyecto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository para Proyecto
 */
@Repository
public interface ProyectoRepository extends JpaRepository<Proyecto, Long> {
    List<Proyecto> findByEmpresaId(Long empresaId);
    List<Proyecto> findByTipoProyectoId(Long tipoProyectoId);
    List<Proyecto> findByEstadoId(Long estadoId);
}
