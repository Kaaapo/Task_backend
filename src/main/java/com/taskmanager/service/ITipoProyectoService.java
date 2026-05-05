package com.taskmanager.service;

import com.taskmanager.dto.TipoProyectoDTO;
import java.util.List;

public interface ITipoProyectoService {
    List<TipoProyectoDTO> findAll();
    TipoProyectoDTO findById(Long id);
    TipoProyectoDTO create(TipoProyectoDTO dto);
    TipoProyectoDTO update(Long id, TipoProyectoDTO dto);
    void delete(Long id);
}
