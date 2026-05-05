package com.taskmanager.service;

import com.taskmanager.dto.MiembroProyectoDTO;
import java.util.List;

public interface IMiembroProyectoService {
    List<MiembroProyectoDTO> findByProyectoId(Long proyectoId);
    MiembroProyectoDTO addMiembro(Long proyectoId, Long usuarioId, String rol);
    MiembroProyectoDTO updateRol(Long id, String rol);
    void removeMiembro(Long id);
}
