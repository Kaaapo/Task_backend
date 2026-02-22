package com.taskmanager.service;

import com.taskmanager.dto.EmpresaDTO;
import com.taskmanager.exception.ResourceNotFoundException;
import com.taskmanager.model.Empresa;
import com.taskmanager.model.Estado;
import com.taskmanager.repository.EmpresaRepository;
import com.taskmanager.repository.EstadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service para Empresa
 */
@Service
@Transactional
public class EmpresaService {
    
    @Autowired
    private EmpresaRepository empresaRepository;
    
    @Autowired
    private EstadoRepository estadoRepository;
    
    /**
     * Obtener todas las empresas con datos enriquecidos
     */
    public List<EmpresaDTO> findAll() {
        return empresaRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Obtener empresa por ID
     */
    public EmpresaDTO findById(Long id) {
        Empresa empresa = empresaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empresa", id));
        return convertToDTO(empresa);
    }
    
    /**
     * Crear nueva empresa
     */
    public EmpresaDTO create(EmpresaDTO dto) {
        Estado estado = estadoRepository.findById(dto.getEstadoId())
                .orElseThrow(() -> new ResourceNotFoundException("Estado", dto.getEstadoId()));
        
        Empresa empresa = new Empresa();
        empresa.setNombre(dto.getNombre());
        empresa.setDescripcion(dto.getDescripcion());
        empresa.setCorreo(dto.getCorreo());
        empresa.setEstado(estado);
        
        Empresa saved = empresaRepository.save(empresa);
        return convertToDTO(saved);
    }
    
    /**
     * Actualizar empresa existente
     */
    public EmpresaDTO update(Long id, EmpresaDTO dto) {
        Empresa empresa = empresaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empresa", id));
        
        Estado estado = estadoRepository.findById(dto.getEstadoId())
                .orElseThrow(() -> new ResourceNotFoundException("Estado", dto.getEstadoId()));
        
        empresa.setNombre(dto.getNombre());
        empresa.setDescripcion(dto.getDescripcion());
        empresa.setCorreo(dto.getCorreo());
        empresa.setEstado(estado);
        
        Empresa updated = empresaRepository.save(empresa);
        return convertToDTO(updated);
    }
    
    /**
     * Eliminar empresa
     */
    public void delete(Long id) {
        if (!empresaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Empresa", id);
        }
        empresaRepository.deleteById(id);
    }
    
    /**
     * Convertir entidad a DTO con datos enriquecidos
     */
    private EmpresaDTO convertToDTO(Empresa empresa) {
        return new EmpresaDTO(
                empresa.getId(),
                empresa.getNombre(),
                empresa.getDescripcion(),
                empresa.getCorreo(),
                empresa.getEstado().getId(),
                empresa.getEstado().getNombre() // Campo enriquecido
        );
    }
}
