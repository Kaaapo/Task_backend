package com.taskmanager.service;

import com.taskmanager.dto.MiembroProyectoDTO;
import com.taskmanager.exception.ResourceNotFoundException;
import com.taskmanager.model.MiembroProyecto;
import com.taskmanager.model.Proyecto;
import com.taskmanager.model.Usuario;
import com.taskmanager.repository.MiembroProyectoRepository;
import com.taskmanager.repository.ProyectoRepository;
import com.taskmanager.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MiembroProyectoService {

    @Autowired
    private MiembroProyectoRepository miembroProyectoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ProyectoRepository proyectoRepository;

    public List<MiembroProyectoDTO> findByProyectoId(Long proyectoId) {
        return miembroProyectoRepository.findByProyectoId(proyectoId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public MiembroProyectoDTO addMiembro(Long proyectoId, Long usuarioId, String rol) {
        if (miembroProyectoRepository.existsByUsuarioIdAndProyectoId(usuarioId, proyectoId)) {
            throw new RuntimeException("El usuario ya es miembro de este proyecto");
        }

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", usuarioId));
        Proyecto proyecto = proyectoRepository.findById(proyectoId)
                .orElseThrow(() -> new ResourceNotFoundException("Proyecto", proyectoId));

        MiembroProyecto miembro = new MiembroProyecto();
        miembro.setUsuario(usuario);
        miembro.setProyecto(proyecto);
        miembro.setRol(rol != null ? rol : "DESARROLLADOR");

        MiembroProyecto saved = miembroProyectoRepository.save(miembro);
        return convertToDTO(saved);
    }

    public MiembroProyectoDTO updateRol(Long id, String rol) {
        MiembroProyecto miembro = miembroProyectoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MiembroProyecto", id));
        miembro.setRol(rol);
        MiembroProyecto updated = miembroProyectoRepository.save(miembro);
        return convertToDTO(updated);
    }

    public void removeMiembro(Long id) {
        if (!miembroProyectoRepository.existsById(id)) {
            throw new ResourceNotFoundException("MiembroProyecto", id);
        }
        miembroProyectoRepository.deleteById(id);
    }

    private MiembroProyectoDTO convertToDTO(MiembroProyecto miembro) {
        return new MiembroProyectoDTO(
                miembro.getId(),
                miembro.getUsuario().getId(),
                miembro.getUsuario().getNombre() + " " + miembro.getUsuario().getApellido(),
                miembro.getUsuario().getApodo(),
                miembro.getUsuario().getEmail(),
                miembro.getProyecto().getId(),
                miembro.getProyecto().getNombre(),
                miembro.getRol(),
                miembro.getFechaAsignacion()
        );
    }
}
