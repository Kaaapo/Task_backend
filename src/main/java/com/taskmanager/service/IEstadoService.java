package com.taskmanager.service;

import com.taskmanager.dto.EstadoDTO;
import java.util.List;

public interface IEstadoService {
    List<EstadoDTO> findAll();
    EstadoDTO findById(Long id);
    EstadoDTO create(EstadoDTO dto);
    EstadoDTO update(Long id, EstadoDTO dto);
    void delete(Long id);
}
