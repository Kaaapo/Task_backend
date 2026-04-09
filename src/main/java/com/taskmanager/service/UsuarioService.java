package com.taskmanager.service;

import com.taskmanager.dto.UsuarioDTO;
import com.taskmanager.exception.ResourceNotFoundException;
import com.taskmanager.model.Usuario;
import com.taskmanager.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<UsuarioDTO> findAll() {
        return usuarioRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public UsuarioDTO findById(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", id));
        return convertToDTO(usuario);
    }

    public UsuarioDTO findByEmail(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con email: " + email));
        return convertToDTO(usuario);
    }

    public UsuarioDTO update(Long id, UsuarioDTO dto) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", id));

        usuario.setNombre(dto.getNombre());
        usuario.setApellido(dto.getApellido());
        usuario.setApodo(dto.getApodo());
        usuario.setAvatarUrl(dto.getAvatarUrl());
        usuario.setTelefono(dto.getTelefono());

        Usuario updated = usuarioRepository.save(usuario);
        return convertToDTO(updated);
    }

    public UsuarioDTO convertToDTO(Usuario usuario) {
        return new UsuarioDTO(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getApodo(),
                usuario.getEmail(),
                usuario.getAvatarUrl(),
                usuario.getTelefono(),
                usuario.getFechaRegistro(),
                usuario.getUltimoAcceso(),
                usuario.getActivo()
        );
    }
}
