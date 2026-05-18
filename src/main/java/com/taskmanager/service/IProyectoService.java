package com.taskmanager.service;

import com.taskmanager.dto.ProyectoDTO;
import java.util.List;

public interface IProyectoService {
    List<ProyectoDTO> findAccessibleForUser(String emailUsuario);

    List<ProyectoDTO> findAccessibleByEmpresaForUser(String emailUsuario, Long empresaId);

    List<ProyectoDTO> findAll();
    ProyectoDTO findById(Long id);
    List<ProyectoDTO> findByEmpresaId(Long empresaId);
    ProyectoDTO create(ProyectoDTO dto, String emailCreador);
    ProyectoDTO update(Long id, ProyectoDTO dto);
    void delete(Long id);
}
