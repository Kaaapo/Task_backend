package com.taskmanager.service;

import com.taskmanager.dto.TareaDTO;
import java.util.List;

public interface ITareaService {
    List<TareaDTO> findAccessibleForUser(String emailUsuario);

    List<TareaDTO> findAll();
    TareaDTO findById(Long id);
    List<TareaDTO> findByProyectoId(Long proyectoId);
    List<TareaDTO> findByAsignadoId(Long asignadoId);
    List<TareaDTO> findByProyectoIdAndEstadoId(Long proyectoId, Long estadoId);
    TareaDTO create(TareaDTO dto, String emailCreador);
    TareaDTO update(Long id, TareaDTO dto);
    void delete(Long id);
}
