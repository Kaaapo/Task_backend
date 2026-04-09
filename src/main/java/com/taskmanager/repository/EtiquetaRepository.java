package com.taskmanager.repository;

import com.taskmanager.model.Etiqueta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EtiquetaRepository extends JpaRepository<Etiqueta, Long> {
    List<Etiqueta> findByEmpresaId(Long empresaId);
}
