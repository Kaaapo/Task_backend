package com.taskmanager.repository;

import com.taskmanager.model.MiembroProyecto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MiembroProyectoRepository extends JpaRepository<MiembroProyecto, Long> {
    List<MiembroProyecto> findByProyectoId(Long proyectoId);
    List<MiembroProyecto> findByUsuarioId(Long usuarioId);
    Optional<MiembroProyecto> findByUsuarioIdAndProyectoId(Long usuarioId, Long proyectoId);
    Boolean existsByUsuarioIdAndProyectoId(Long usuarioId, Long proyectoId);

    @Modifying
    @Query(value = "DELETE FROM miembros_proyecto WHERE proyecto_id = :proyectoId", nativeQuery = true)
    void deleteByProyectoId(@Param("proyectoId") Long proyectoId);
}
