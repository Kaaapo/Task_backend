package com.taskmanager.service;

import com.taskmanager.dto.ComentarioTareaDTO;
import java.util.List;

public interface IComentarioTareaService {
    List<ComentarioTareaDTO> findByTareaId(Long tareaId);
    ComentarioTareaDTO create(Long tareaId, ComentarioTareaDTO dto, String emailAutor);
    ComentarioTareaDTO update(Long id, ComentarioTareaDTO dto);
    void delete(Long id);
}
