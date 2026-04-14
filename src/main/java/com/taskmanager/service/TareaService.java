package com.taskmanager.service;

import com.taskmanager.dto.TareaDTO;
import com.taskmanager.exception.ResourceNotFoundException;
import com.taskmanager.model.*;
import com.taskmanager.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class TareaService {

    @Autowired
    private TareaRepository tareaRepository;

    @Autowired
    private ProyectoRepository proyectoRepository;

    @Autowired
    private EstadoRepository estadoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EtiquetaRepository etiquetaRepository;

    public List<TareaDTO> findAll() {
        return tareaRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public TareaDTO findById(Long id) {
        Tarea tarea = tareaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tarea", id));
        return convertToDTO(tarea);
    }

    public List<TareaDTO> findByProyectoId(Long proyectoId) {
        return tareaRepository.findByProyectoIdOrderByOrdenAsc(proyectoId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<TareaDTO> findByAsignadoId(Long asignadoId) {
        return tareaRepository.findByAsignadoId(asignadoId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<TareaDTO> findByProyectoIdAndEstadoId(Long proyectoId, Long estadoId) {
        return tareaRepository.findByProyectoIdAndEstadoId(proyectoId, estadoId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public TareaDTO create(TareaDTO dto, String emailCreador) {
        Proyecto proyecto = proyectoRepository.findById(dto.getProyectoId())
                .orElseThrow(() -> new ResourceNotFoundException("Proyecto", dto.getProyectoId()));

        Estado estado = estadoRepository.findById(dto.getEstadoId())
                .orElseThrow(() -> new ResourceNotFoundException("Estado", dto.getEstadoId()));

        Usuario creador = usuarioRepository.findByEmail(emailCreador)
                .orElseThrow(() -> new RuntimeException("No se pudo identificar tu cuenta de usuario. Por favor, inicia sesión nuevamente."));

        Tarea tarea = new Tarea();
        tarea.setTitulo(dto.getTitulo());
        tarea.setDescripcion(dto.getDescripcion());
        tarea.setProyecto(proyecto);
        tarea.setEstado(estado);
        tarea.setCreador(creador);
        tarea.setPrioridad(dto.getPrioridad());
        tarea.setFechaLimite(dto.getFechaLimite());
        tarea.setOrden(dto.getOrden());

        if (dto.getAsignadoId() != null) {
            Usuario asignado = usuarioRepository.findById(dto.getAsignadoId())
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario", dto.getAsignadoId()));
            tarea.setAsignado(asignado);
        }

        if (dto.getEtiquetaIds() != null && !dto.getEtiquetaIds().isEmpty()) {
            Set<Etiqueta> etiquetas = new HashSet<>(etiquetaRepository.findAllById(dto.getEtiquetaIds()));
            tarea.setEtiquetas(etiquetas);
        }

        Tarea saved = tareaRepository.save(tarea);
        return convertToDTO(saved);
    }

    public TareaDTO update(Long id, TareaDTO dto) {
        Tarea tarea = tareaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tarea", id));

        Estado estado = estadoRepository.findById(dto.getEstadoId())
                .orElseThrow(() -> new ResourceNotFoundException("Estado", dto.getEstadoId()));

        tarea.setTitulo(dto.getTitulo());
        tarea.setDescripcion(dto.getDescripcion());
        tarea.setEstado(estado);
        tarea.setPrioridad(dto.getPrioridad());
        tarea.setFechaLimite(dto.getFechaLimite());
        tarea.setFechaCompletada(dto.getFechaCompletada());
        tarea.setOrden(dto.getOrden());

        if (dto.getAsignadoId() != null) {
            Usuario asignado = usuarioRepository.findById(dto.getAsignadoId())
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario", dto.getAsignadoId()));
            tarea.setAsignado(asignado);
        } else {
            tarea.setAsignado(null);
        }

        if (dto.getEtiquetaIds() != null) {
            Set<Etiqueta> etiquetas = new HashSet<>(etiquetaRepository.findAllById(dto.getEtiquetaIds()));
            tarea.setEtiquetas(etiquetas);
        }

        Tarea updated = tareaRepository.save(tarea);
        return convertToDTO(updated);
    }

    public void delete(Long id) {
        if (!tareaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Tarea", id);
        }
        tareaRepository.deleteById(id);
    }

    private TareaDTO convertToDTO(Tarea tarea) {
        TareaDTO dto = new TareaDTO();
        dto.setId(tarea.getId());
        dto.setTitulo(tarea.getTitulo());
        dto.setDescripcion(tarea.getDescripcion());
        dto.setProyectoId(tarea.getProyecto().getId());
        dto.setProyectoNombre(tarea.getProyecto().getNombre());
        dto.setEstadoId(tarea.getEstado().getId());
        dto.setEstadoNombre(tarea.getEstado().getNombre());
        if (tarea.getAsignado() != null) {
            dto.setAsignadoId(tarea.getAsignado().getId());
            dto.setAsignadoNombre(tarea.getAsignado().getApodo() != null
                    ? tarea.getAsignado().getApodo()
                    : tarea.getAsignado().getNombre());
        }
        if (tarea.getCreador() != null) {
            dto.setCreadorId(tarea.getCreador().getId());
            dto.setCreadorNombre(tarea.getCreador().getApodo() != null
                    ? tarea.getCreador().getApodo()
                    : tarea.getCreador().getNombre());
        }
        dto.setPrioridad(tarea.getPrioridad());
        dto.setFechaLimite(tarea.getFechaLimite());
        dto.setFechaCompletada(tarea.getFechaCompletada());
        dto.setOrden(tarea.getOrden());
        dto.setFechaCreacion(tarea.getFechaCreacion());
        if (tarea.getEtiquetas() != null) {
            dto.setEtiquetaIds(tarea.getEtiquetas().stream()
                    .map(Etiqueta::getId)
                    .collect(Collectors.toList()));
        }
        return dto;
    }
}
