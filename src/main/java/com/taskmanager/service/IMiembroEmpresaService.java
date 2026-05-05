package com.taskmanager.service;

import com.taskmanager.dto.MiembroEmpresaDTO;
import java.util.List;

public interface IMiembroEmpresaService {
    List<MiembroEmpresaDTO> findByEmpresaId(Long empresaId);
    List<MiembroEmpresaDTO> findByUsuarioId(Long usuarioId);
    MiembroEmpresaDTO addMiembro(Long empresaId, Long usuarioId, String rol);
    MiembroEmpresaDTO updateRol(Long id, String rol);
    void removeMiembro(Long id);
}
