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

@Service
@Transactional
public class EstadoService {

    @Autowired
    private EstadoRepository estadoRepository;

    public List<EstadoDTO> findAll() {
        return estadoRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public EstadoDTO findById(Long id) {
        Estado estado = estadoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Estado", id));
        return convertToDTO(estado);
    }

    public EstadoDTO create(EstadoDTO dto) {
        Estado estado = new Estado();
        estado.setNombre(dto.getNombre());
        estado.setDescripcion(dto.getDescripcion());
        estado.setColor(dto.getColor());
        estado.setOrden(dto.getOrden());

        Estado saved = estadoRepository.save(estado);
        return convertToDTO(saved);
    }

    public EstadoDTO update(Long id, EstadoDTO dto) {
        Estado estado = estadoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Estado", id));

        estado.setNombre(dto.getNombre());
        estado.setDescripcion(dto.getDescripcion());
        estado.setColor(dto.getColor());
        estado.setOrden(dto.getOrden());

        Estado updated = estadoRepository.save(estado);
        return convertToDTO(updated);
    }

    public void delete(Long id) {
        if (!estadoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Estado", id);
        }
        estadoRepository.deleteById(id);
    }

    private EstadoDTO convertToDTO(Estado estado) {
        return new EstadoDTO(
                estado.getId(),
                estado.getNombre(),
                estado.getDescripcion(),
                estado.getColor(),
                estado.getOrden()
        );
    }
}
