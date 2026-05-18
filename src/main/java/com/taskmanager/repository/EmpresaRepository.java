package com.taskmanager.repository;

import com.taskmanager.model.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, Long> {
    List<Empresa> findByCreadorId(Long creadorId);

    @Query("SELECT DISTINCT e FROM Empresa e WHERE e.creador.id = :usuarioId OR EXISTS (" +
            "SELECT 1 FROM MiembroEmpresa m WHERE m.empresa = e AND m.usuario.id = :usuarioId AND m.activo = true)")
    List<Empresa> findAccessibleByUsuarioId(@Param("usuarioId") Long usuarioId);
}
