package com.taskmanager.service;

import com.taskmanager.dto.EtiquetaDTO;
import com.taskmanager.exception.ResourceNotFoundException;
import com.taskmanager.model.Empresa;
import com.taskmanager.model.Etiqueta;
import com.taskmanager.repository.EmpresaRepository;
import com.taskmanager.repository.EtiquetaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class EtiquetaService {

    @Autowired
    private EtiquetaRepository etiquetaRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    public List<EtiquetaDTO> findAll() {
        return etiquetaRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<EtiquetaDTO> findByEmpresaId(Long empresaId) {
        return etiquetaRepository.findByEmpresaId(empresaId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public EtiquetaDTO findById(Long id) {
        Etiqueta etiqueta = etiquetaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Etiqueta", id));
        return convertToDTO(etiqueta);
    }

    public EtiquetaDTO create(EtiquetaDTO dto) {
        Empresa empresa = empresaRepository.findById(dto.getEmpresaId())
                .orElseThrow(() -> new ResourceNotFoundException("Empresa", dto.getEmpresaId()));

        Etiqueta etiqueta = new Etiqueta();
        etiqueta.setNombre(dto.getNombre());
        etiqueta.setColor(dto.getColor());
        etiqueta.setEmpresa(empresa);

        Etiqueta saved = etiquetaRepository.save(etiqueta);
        return convertToDTO(saved);
    }

    public EtiquetaDTO update(Long id, EtiquetaDTO dto) {
        Etiqueta etiqueta = etiquetaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Etiqueta", id));

        etiqueta.setNombre(dto.getNombre());
        etiqueta.setColor(dto.getColor());

        Etiqueta updated = etiquetaRepository.save(etiqueta);
        return convertToDTO(updated);
    }

    public void delete(Long id) {
        if (!etiquetaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Etiqueta", id);
        }
        etiquetaRepository.deleteById(id);
    }

    private EtiquetaDTO convertToDTO(Etiqueta etiqueta) {
        return new EtiquetaDTO(
                etiqueta.getId(),
                etiqueta.getNombre(),
                etiqueta.getColor(),
                etiqueta.getEmpresa().getId(),
                etiqueta.getEmpresa().getNombre()
        );
    }
}
