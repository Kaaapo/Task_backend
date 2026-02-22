package com.taskmanager.service;

import com.taskmanager.dto.RamaDTO;
import com.taskmanager.exception.ResourceNotFoundException;
import com.taskmanager.model.Rama;
import com.taskmanager.repository.RamaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service para Rama
 */
@Service
@Transactional
public class RamaService {
    
    @Autowired
    private RamaRepository ramaRepository;
    
    public List<RamaDTO> findAll() {
        return ramaRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public RamaDTO findById(Long id) {
        Rama rama = ramaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rama", id));
        return convertToDTO(rama);
    }
    
    public RamaDTO create(RamaDTO dto) {
        Rama rama = new Rama();
        rama.setNombre(dto.getNombre());
        rama.setDescripcion(dto.getDescripcion());
        
        Rama saved = ramaRepository.save(rama);
        return convertToDTO(saved);
    }
    
    public RamaDTO update(Long id, RamaDTO dto) {
        Rama rama = ramaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rama", id));
        
        rama.setNombre(dto.getNombre());
        rama.setDescripcion(dto.getDescripcion());
        
        Rama updated = ramaRepository.save(rama);
        return convertToDTO(updated);
    }
    
    public void delete(Long id) {
        if (!ramaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Rama", id);
        }
        ramaRepository.deleteById(id);
    }
    
    private RamaDTO convertToDTO(Rama rama) {
        return new RamaDTO(
                rama.getId(),
                rama.getNombre(),
                rama.getDescripcion()
        );
    }
}
