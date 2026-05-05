package com.taskmanager.service;

import com.taskmanager.dto.EmpresaDTO;
import java.util.List;

public interface IEmpresaService {
    List<EmpresaDTO> findAll();
    EmpresaDTO findById(Long id);
    List<EmpresaDTO> findByCreadorId(Long creadorId);
    EmpresaDTO create(EmpresaDTO dto, String emailCreador);
    EmpresaDTO update(Long id, EmpresaDTO dto);
    void delete(Long id);
}
