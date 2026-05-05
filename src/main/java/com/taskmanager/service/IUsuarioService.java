package com.taskmanager.service;

import com.taskmanager.dto.UsuarioDTO;
import com.taskmanager.model.Usuario;
import java.util.List;

public interface IUsuarioService {
    List<UsuarioDTO> findAll();
    UsuarioDTO findById(Long id);
    UsuarioDTO findByEmail(String email);
    UsuarioDTO update(Long id, UsuarioDTO dto);
    UsuarioDTO convertToDTO(Usuario usuario);
}
