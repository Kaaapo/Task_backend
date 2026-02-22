package com.taskmanager.service;

import com.taskmanager.dto.FaseDTO;
import com.taskmanager.exception.ResourceNotFoundException;
import com.taskmanager.model.Estado;
import com.taskmanager.model.Fase;
import com.taskmanager.repository.EstadoRepository;
import com.taskmanager.repository.FaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service para Fase
 */
@Service
@Transactional
public class FaseService {
    
    @Autowired
    private FaseRepository faseRepository;
    
    @Autowired
    private EstadoRepository estadoRepository;
    
    public List<FaseDTO> findAll() {
        return faseRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public FaseDTO findById(Long id) {
        Fase fase = faseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fase", id));
        return convertToDTO(fase);
    }
    
    public FaseDTO create(FaseDTO dto) {
        Estado estado = estadoRepository.findById(dto.getEstadoId())
                .orElseThrow(() -> new ResourceNotFoundException("Estado", dto.getEstadoId()));
        
        Fase fase = new Fase();
        fase.setNombre(dto.getNombre());
        fase.setDescripcion(dto.getDescripcion());
        fase.setEstado(estado);
        
        Fase saved = faseRepository.save(fase);
        return convertToDTO(saved);
    }
    
    public FaseDTO update(Long id, FaseDTO dto) {
        Fase fase = faseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fase", id));
        
        Estado estado = estadoRepository.findById(dto.getEstadoId())
                .orElseThrow(() -> new ResourceNotFoundException("Estado", dto.getEstadoId()));
        
        fase.setNombre(dto.getNombre());
        fase.setDescripcion(dto.getDescripcion());
        fase.setEstado(estado);
        
        Fase updated = faseRepository.save(fase);
        return convertToDTO(updated);
    }
    
    public void delete(Long id) {
        if (!faseRepository.existsById(id)) {
            throw new ResourceNotFoundException("Fase", id);
        }
        faseRepository.deleteById(id);
    }
    
    private FaseDTO convertToDTO(Fase fase) {
        return new FaseDTO(
                fase.getId(),
                fase.getNombre(),
                fase.getDescripcion(),
                fase.getEstado().getId(),
                fase.getEstado().getNombre()
        );
    }
}
