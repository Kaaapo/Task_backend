package com.taskmanager.service;

import com.taskmanager.dto.SistemaDTO;
import com.taskmanager.exception.ResourceNotFoundException;
import com.taskmanager.model.Estado;
import com.taskmanager.model.Sistema;
import com.taskmanager.repository.EstadoRepository;
import com.taskmanager.repository.SistemaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service para Sistema
 */
@Service
@Transactional
public class SistemaService {
    
    @Autowired
    private SistemaRepository sistemaRepository;
    
    @Autowired
    private EstadoRepository estadoRepository;
    
    public List<SistemaDTO> findAll() {
        return sistemaRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public SistemaDTO findById(Long id) {
        Sistema sistema = sistemaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sistema", id));
        return convertToDTO(sistema);
    }
    
    public SistemaDTO create(SistemaDTO dto) {
        Estado estado = estadoRepository.findById(dto.getEstadoId())
                .orElseThrow(() -> new ResourceNotFoundException("Estado", dto.getEstadoId()));
        
        Sistema sistema = new Sistema();
        sistema.setNombre(dto.getNombre());
        sistema.setDescripcion(dto.getDescripcion());
        sistema.setEstado(estado);
        
        Sistema saved = sistemaRepository.save(sistema);
        return convertToDTO(saved);
    }
    
    public SistemaDTO update(Long id, SistemaDTO dto) {
        Sistema sistema = sistemaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sistema", id));
        
        Estado estado = estadoRepository.findById(dto.getEstadoId())
                .orElseThrow(() -> new ResourceNotFoundException("Estado", dto.getEstadoId()));
        
        sistema.setNombre(dto.getNombre());
        sistema.setDescripcion(dto.getDescripcion());
        sistema.setEstado(estado);
        
        Sistema updated = sistemaRepository.save(sistema);
        return convertToDTO(updated);
    }
    
    public void delete(Long id) {
        if (!sistemaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Sistema", id);
        }
        sistemaRepository.deleteById(id);
    }
    
    private SistemaDTO convertToDTO(Sistema sistema) {
        return new SistemaDTO(
                sistema.getId(),
                sistema.getNombre(),
                sistema.getDescripcion(),
                sistema.getEstado().getId(),
                sistema.getEstado().getNombre()
        );
    }
}
