package com.taskmanager.service;

import com.taskmanager.dto.EstadoDTO;
import com.taskmanager.exception.ResourceNotFoundException;
import com.taskmanager.model.Estado;
import com.taskmanager.repository.EstadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service para Estado
 * Contiene la lógica de negocio
 */
@Service
@Transactional
public class EstadoService {
    
    @Autowired
    private EstadoRepository estadoRepository;
    
    /**
     * Obtener todos los estados
     */
    public List<EstadoDTO> findAll() {
        return estadoRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Obtener estado por ID
     */
    public EstadoDTO findById(Long id) {
        Estado estado = estadoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Estado", id));
        return convertToDTO(estado);
    }
    
    /**
     * Crear nuevo estado
     */
    public EstadoDTO create(EstadoDTO dto) {
        Estado estado = new Estado();
        estado.setNombre(dto.getNombre());
        estado.setDescripcion(dto.getDescripcion());
        
        Estado saved = estadoRepository.save(estado);
        return convertToDTO(saved);
    }
    
    /**
     * Actualizar estado existente
     */
    public EstadoDTO update(Long id, EstadoDTO dto) {
        Estado estado = estadoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Estado", id));
        
        estado.setNombre(dto.getNombre());
        estado.setDescripcion(dto.getDescripcion());
        
        Estado updated = estadoRepository.save(estado);
        return convertToDTO(updated);
    }
    
    /**
     * Eliminar estado
     */
    public void delete(Long id) {
        if (!estadoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Estado", id);
        }
        estadoRepository.deleteById(id);
    }
    
    /**
     * Convertir entidad a DTO
     */
    private EstadoDTO convertToDTO(Estado estado) {
        return new EstadoDTO(
                estado.getId(),
                estado.getNombre(),
                estado.getDescripcion()
        );
    }
}
