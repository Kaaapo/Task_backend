package com.taskmanager.service;

import com.taskmanager.dto.MiembroEmpresaDTO;
import com.taskmanager.exception.ResourceNotFoundException;
import com.taskmanager.model.Empresa;
import com.taskmanager.model.MiembroEmpresa;
import com.taskmanager.model.Usuario;
import com.taskmanager.repository.EmpresaRepository;
import com.taskmanager.repository.MiembroEmpresaRepository;
import com.taskmanager.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MiembroEmpresaService {

    @Autowired
    private MiembroEmpresaRepository miembroEmpresaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    public List<MiembroEmpresaDTO> findByEmpresaId(Long empresaId) {
        return miembroEmpresaRepository.findByEmpresaId(empresaId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<MiembroEmpresaDTO> findByUsuarioId(Long usuarioId) {
        return miembroEmpresaRepository.findByUsuarioId(usuarioId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public MiembroEmpresaDTO addMiembro(Long empresaId, Long usuarioId, String rol) {
        if (miembroEmpresaRepository.existsByUsuarioIdAndEmpresaId(usuarioId, empresaId)) {
            throw new RuntimeException("El usuario ya es miembro de esta empresa");
        }

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", usuarioId));
        Empresa empresa = empresaRepository.findById(empresaId)
                .orElseThrow(() -> new ResourceNotFoundException("Empresa", empresaId));

        MiembroEmpresa miembro = new MiembroEmpresa();
        miembro.setUsuario(usuario);
        miembro.setEmpresa(empresa);
        miembro.setRol(rol != null ? rol : "MIEMBRO");

        MiembroEmpresa saved = miembroEmpresaRepository.save(miembro);
        return convertToDTO(saved);
    }

    public MiembroEmpresaDTO updateRol(Long id, String rol) {
        MiembroEmpresa miembro = miembroEmpresaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MiembroEmpresa", id));
        miembro.setRol(rol);
        MiembroEmpresa updated = miembroEmpresaRepository.save(miembro);
        return convertToDTO(updated);
    }

    public void removeMiembro(Long id) {
        if (!miembroEmpresaRepository.existsById(id)) {
            throw new ResourceNotFoundException("MiembroEmpresa", id);
        }
        miembroEmpresaRepository.deleteById(id);
    }

    private MiembroEmpresaDTO convertToDTO(MiembroEmpresa miembro) {
        return new MiembroEmpresaDTO(
                miembro.getId(),
                miembro.getUsuario().getId(),
                miembro.getUsuario().getNombre() + " " + miembro.getUsuario().getApellido(),
                miembro.getUsuario().getApodo(),
                miembro.getUsuario().getEmail(),
                miembro.getEmpresa().getId(),
                miembro.getEmpresa().getNombre(),
                miembro.getRol(),
                miembro.getFechaUnion(),
                miembro.getActivo()
        );
    }
}
