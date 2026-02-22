package com.taskmanager.service;

import com.taskmanager.dto.TipoProyectoDTO;
import com.taskmanager.exception.ResourceNotFoundException;
import com.taskmanager.model.Estado;
import com.taskmanager.model.TipoProyecto;
import com.taskmanager.repository.EstadoRepository;
import com.taskmanager.repository.TipoProyectoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service para TipoProyecto
 */
@Service
@Transactional
public class TipoProyectoService {
    
    @Autowired
    private TipoProyectoRepository tipoProyectoRepository;
    
    @Autowired
    private EstadoRepository estadoRepository;
    
    public List<TipoProyectoDTO> findAll() {
        return tipoProyectoRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public TipoProyectoDTO findById(Long id) {
        TipoProyecto tipoProyecto = tipoProyectoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TipoProyecto", id));
        return convertToDTO(tipoProyecto);
    }
    
    public TipoProyectoDTO create(TipoProyectoDTO dto) {
        Estado estado = estadoRepository.findById(dto.getEstadoId())
                .orElseThrow(() -> new ResourceNotFoundException("Estado", dto.getEstadoId()));
        
        TipoProyecto tipoProyecto = new TipoProyecto();
        tipoProyecto.setNombre(dto.getNombre());
        tipoProyecto.setDescripcion(dto.getDescripcion());
        tipoProyecto.setEstado(estado);
        
        TipoProyecto saved = tipoProyectoRepository.save(tipoProyecto);
        return convertToDTO(saved);
    }
    
    public TipoProyectoDTO update(Long id, TipoProyectoDTO dto) {
        TipoProyecto tipoProyecto = tipoProyectoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TipoProyecto", id));
        
        Estado estado = estadoRepository.findById(dto.getEstadoId())
                .orElseThrow(() -> new ResourceNotFoundException("Estado", dto.getEstadoId()));
        
        tipoProyecto.setNombre(dto.getNombre());
        tipoProyecto.setDescripcion(dto.getDescripcion());
        tipoProyecto.setEstado(estado);
        
        TipoProyecto updated = tipoProyectoRepository.save(tipoProyecto);
        return convertToDTO(updated);
    }
    
    public void delete(Long id) {
        if (!tipoProyectoRepository.existsById(id)) {
            throw new ResourceNotFoundException("TipoProyecto", id);
        }
        tipoProyectoRepository.deleteById(id);
    }
    
    private TipoProyectoDTO convertToDTO(TipoProyecto tipoProyecto) {
        return new TipoProyectoDTO(
                tipoProyecto.getId(),
                tipoProyecto.getNombre(),
                tipoProyecto.getDescripcion(),
                tipoProyecto.getEstado().getId(),
                tipoProyecto.getEstado().getNombre()
        );
    }
}
