package com.taskmanager.service;

import com.taskmanager.dto.SubsistemaDTO;
import com.taskmanager.exception.ResourceNotFoundException;
import com.taskmanager.model.Estado;
import com.taskmanager.model.Sistema;
import com.taskmanager.model.Subsistema;
import com.taskmanager.repository.EstadoRepository;
import com.taskmanager.repository.SistemaRepository;
import com.taskmanager.repository.SubsistemaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service para Subsistema
 */
@Service
@Transactional
public class SubsistemaService {
    
    @Autowired
    private SubsistemaRepository subsistemaRepository;
    
    @Autowired
    private SistemaRepository sistemaRepository;
    
    @Autowired
    private EstadoRepository estadoRepository;
    
    public List<SubsistemaDTO> findAll() {
        return subsistemaRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public SubsistemaDTO findById(Long id) {
        Subsistema subsistema = subsistemaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subsistema", id));
        return convertToDTO(subsistema);
    }
    
    public SubsistemaDTO create(SubsistemaDTO dto) {
        Sistema sistema = sistemaRepository.findById(dto.getSistemaId())
                .orElseThrow(() -> new ResourceNotFoundException("Sistema", dto.getSistemaId()));
        
        Estado estado = estadoRepository.findById(dto.getEstadoId())
                .orElseThrow(() -> new ResourceNotFoundException("Estado", dto.getEstadoId()));
        
        Subsistema subsistema = new Subsistema();
        subsistema.setNombre(dto.getNombre());
        subsistema.setDescripcion(dto.getDescripcion());
        subsistema.setSistema(sistema);
        subsistema.setEstado(estado);
        
        Subsistema saved = subsistemaRepository.save(subsistema);
        return convertToDTO(saved);
    }
    
    public SubsistemaDTO update(Long id, SubsistemaDTO dto) {
        Subsistema subsistema = subsistemaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subsistema", id));
        
        Sistema sistema = sistemaRepository.findById(dto.getSistemaId())
                .orElseThrow(() -> new ResourceNotFoundException("Sistema", dto.getSistemaId()));
        
        Estado estado = estadoRepository.findById(dto.getEstadoId())
                .orElseThrow(() -> new ResourceNotFoundException("Estado", dto.getEstadoId()));
        
        subsistema.setNombre(dto.getNombre());
        subsistema.setDescripcion(dto.getDescripcion());
        subsistema.setSistema(sistema);
        subsistema.setEstado(estado);
        
        Subsistema updated = subsistemaRepository.save(subsistema);
        return convertToDTO(updated);
    }
    
    public void delete(Long id) {
        if (!subsistemaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Subsistema", id);
        }
        subsistemaRepository.deleteById(id);
    }
    
    private SubsistemaDTO convertToDTO(Subsistema subsistema) {
        return new SubsistemaDTO(
                subsistema.getId(),
                subsistema.getNombre(),
                subsistema.getDescripcion(),
                subsistema.getSistema().getId(),
                subsistema.getSistema().getNombre(),
                subsistema.getEstado().getId(),
                subsistema.getEstado().getNombre()
        );
    }
}
