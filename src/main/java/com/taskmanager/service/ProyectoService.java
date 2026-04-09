package com.taskmanager.service;

import com.taskmanager.dto.ProyectoDTO;
import com.taskmanager.exception.ResourceNotFoundException;
import com.taskmanager.model.*;
import com.taskmanager.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProyectoService {

    @Autowired
    private ProyectoRepository proyectoRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private TipoProyectoRepository tipoProyectoRepository;

    @Autowired
    private EstadoRepository estadoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<ProyectoDTO> findAll() {
        return proyectoRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ProyectoDTO findById(Long id) {
        Proyecto proyecto = proyectoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Proyecto", id));
        return convertToDTO(proyecto);
    }

    public List<ProyectoDTO> findByEmpresaId(Long empresaId) {
        return proyectoRepository.findByEmpresaId(empresaId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ProyectoDTO create(ProyectoDTO dto, String emailCreador) {
        Empresa empresa = empresaRepository.findById(dto.getEmpresaId())
                .orElseThrow(() -> new ResourceNotFoundException("Empresa", dto.getEmpresaId()));

        TipoProyecto tipoProyecto = tipoProyectoRepository.findById(dto.getTipoProyectoId())
                .orElseThrow(() -> new ResourceNotFoundException("TipoProyecto", dto.getTipoProyectoId()));

        Estado estado = estadoRepository.findById(dto.getEstadoId())
                .orElseThrow(() -> new ResourceNotFoundException("Estado", dto.getEstadoId()));

        Usuario creador = usuarioRepository.findByEmail(emailCreador)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Proyecto proyecto = new Proyecto();
        proyecto.setNombre(dto.getNombre());
        proyecto.setDescripcion(dto.getDescripcion());
        proyecto.setCodigo(dto.getCodigo());
        proyecto.setEmpresa(empresa);
        proyecto.setTipoProyecto(tipoProyecto);
        proyecto.setEstado(estado);
        proyecto.setCreador(creador);
        proyecto.setPrioridad(dto.getPrioridad());
        proyecto.setFechaInicio(dto.getFechaInicio());
        proyecto.setFechaFinEstimada(dto.getFechaFinEstimada());
        proyecto.setFechaFinReal(dto.getFechaFinReal());
        proyecto.setProgreso(dto.getProgreso());

        Proyecto saved = proyectoRepository.save(proyecto);
        return convertToDTO(saved);
    }

    public ProyectoDTO update(Long id, ProyectoDTO dto) {
        Proyecto proyecto = proyectoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Proyecto", id));

        Empresa empresa = empresaRepository.findById(dto.getEmpresaId())
                .orElseThrow(() -> new ResourceNotFoundException("Empresa", dto.getEmpresaId()));

        TipoProyecto tipoProyecto = tipoProyectoRepository.findById(dto.getTipoProyectoId())
                .orElseThrow(() -> new ResourceNotFoundException("TipoProyecto", dto.getTipoProyectoId()));

        Estado estado = estadoRepository.findById(dto.getEstadoId())
                .orElseThrow(() -> new ResourceNotFoundException("Estado", dto.getEstadoId()));

        proyecto.setNombre(dto.getNombre());
        proyecto.setDescripcion(dto.getDescripcion());
        proyecto.setCodigo(dto.getCodigo());
        proyecto.setEmpresa(empresa);
        proyecto.setTipoProyecto(tipoProyecto);
        proyecto.setEstado(estado);
        proyecto.setPrioridad(dto.getPrioridad());
        proyecto.setFechaInicio(dto.getFechaInicio());
        proyecto.setFechaFinEstimada(dto.getFechaFinEstimada());
        proyecto.setFechaFinReal(dto.getFechaFinReal());
        proyecto.setProgreso(dto.getProgreso());

        Proyecto updated = proyectoRepository.save(proyecto);
        return convertToDTO(updated);
    }

    public void delete(Long id) {
        if (!proyectoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Proyecto", id);
        }
        proyectoRepository.deleteById(id);
    }

    private ProyectoDTO convertToDTO(Proyecto proyecto) {
        ProyectoDTO dto = new ProyectoDTO();
        dto.setId(proyecto.getId());
        dto.setNombre(proyecto.getNombre());
        dto.setDescripcion(proyecto.getDescripcion());
        dto.setCodigo(proyecto.getCodigo());
        dto.setEmpresaId(proyecto.getEmpresa().getId());
        dto.setEmpresaNombre(proyecto.getEmpresa().getNombre());
        dto.setTipoProyectoId(proyecto.getTipoProyecto().getId());
        dto.setTipoProyectoNombre(proyecto.getTipoProyecto().getNombre());
        dto.setEstadoId(proyecto.getEstado().getId());
        dto.setEstadoNombre(proyecto.getEstado().getNombre());
        if (proyecto.getCreador() != null) {
            dto.setCreadorId(proyecto.getCreador().getId());
            dto.setCreadorNombre(proyecto.getCreador().getApodo() != null
                    ? proyecto.getCreador().getApodo()
                    : proyecto.getCreador().getNombre());
        }
        dto.setPrioridad(proyecto.getPrioridad());
        dto.setFechaInicio(proyecto.getFechaInicio());
        dto.setFechaFinEstimada(proyecto.getFechaFinEstimada());
        dto.setFechaFinReal(proyecto.getFechaFinReal());
        dto.setProgreso(proyecto.getProgreso());
        dto.setFechaCreacion(proyecto.getFechaCreacion());
        return dto;
    }
}
