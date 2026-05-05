package com.taskmanager.repository;

import com.taskmanager.model.MiembroEmpresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MiembroEmpresaRepository extends JpaRepository<MiembroEmpresa, Long> {
    List<MiembroEmpresa> findByEmpresaId(Long empresaId);
    List<MiembroEmpresa> findByUsuarioId(Long usuarioId);
    Optional<MiembroEmpresa> findByUsuarioIdAndEmpresaId(Long usuarioId, Long empresaId);
    Boolean existsByUsuarioIdAndEmpresaId(Long usuarioId, Long empresaId);

    @Modifying
    @Query(value = "DELETE FROM miembros_empresa WHERE empresa_id = :empresaId", nativeQuery = true)
    void deleteByEmpresaId(@Param("empresaId") Long empresaId);
}
