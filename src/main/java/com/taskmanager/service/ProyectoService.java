package com.taskmanager.service;

import com.taskmanager.dto.ProyectoDTO;
import com.taskmanager.exception.ResourceNotFoundException;
import com.taskmanager.model.Empresa;
import com.taskmanager.model.Estado;
import com.taskmanager.model.Proyecto;
import com.taskmanager.model.TipoProyecto;
import com.taskmanager.repository.EmpresaRepository;
import com.taskmanager.repository.EstadoRepository;
import com.taskmanager.repository.ProyectoRepository;
import com.taskmanager.repository.TipoProyectoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service para Proyecto
 */
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
    
    public ProyectoDTO create(ProyectoDTO dto) {
        Empresa empresa = empresaRepository.findById(dto.getEmpresaId())
                .orElseThrow(() -> new ResourceNotFoundException("Empresa", dto.getEmpresaId()));
        
        TipoProyecto tipoProyecto = tipoProyectoRepository.findById(dto.getTipoProyectoId())
                .orElseThrow(() -> new ResourceNotFoundException("TipoProyecto", dto.getTipoProyectoId()));
        
        Estado estado = estadoRepository.findById(dto.getEstadoId())
                .orElseThrow(() -> new ResourceNotFoundException("Estado", dto.getEstadoId()));
        
        Proyecto proyecto = new Proyecto();
        proyecto.setNombre(dto.getNombre());
        proyecto.setDescripcion(dto.getDescripcion());
        proyecto.setEmpresa(empresa);
        proyecto.setTipoProyecto(tipoProyecto);
        proyecto.setEstado(estado);
        
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
        proyecto.setEmpresa(empresa);
        proyecto.setTipoProyecto(tipoProyecto);
        proyecto.setEstado(estado);
        
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
        return new ProyectoDTO(
                proyecto.getId(),
                proyecto.getNombre(),
                proyecto.getDescripcion(),
                proyecto.getEmpresa().getId(),
                proyecto.getEmpresa().getNombre(),
                proyecto.getTipoProyecto().getId(),
                proyecto.getTipoProyecto().getNombre(),
                proyecto.getEstado().getId(),
                proyecto.getEstado().getNombre()
        );
    }
}
