package com.taskmanager.service;

import com.taskmanager.dto.EmpresaDTO;
import com.taskmanager.exception.ResourceNotFoundException;
import com.taskmanager.model.Empresa;
import com.taskmanager.model.Usuario;
import com.taskmanager.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class EmpresaService implements IEmpresaService {

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ProyectoRepository proyectoRepository;

    @Autowired
    private TareaRepository tareaRepository;

    @Autowired
    private ComentarioTareaRepository comentarioTareaRepository;

    @Autowired
    private MiembroProyectoRepository miembroProyectoRepository;

    @Autowired
    private MiembroEmpresaRepository miembroEmpresaRepository;

    public List<EmpresaDTO> findAll() {
        return empresaRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public EmpresaDTO findById(Long id) {
        Empresa empresa = empresaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empresa", id));
        return convertToDTO(empresa);
    }

    public List<EmpresaDTO> findByCreadorId(Long creadorId) {
        return empresaRepository.findByCreadorId(creadorId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public EmpresaDTO create(EmpresaDTO dto, String emailCreador) {
        Usuario creador = usuarioRepository.findByEmail(emailCreador)
                .orElseThrow(() -> new RuntimeException("No se pudo identificar tu cuenta de usuario. Por favor, inicia sesión nuevamente."));

        Empresa empresa = new Empresa();
        empresa.setNombre(dto.getNombre());
        empresa.setDescripcion(dto.getDescripcion());
        empresa.setNit(dto.getNit());
        empresa.setCorreo(dto.getCorreo());
        empresa.setTelefono(dto.getTelefono());
        empresa.setDireccion(dto.getDireccion());
        empresa.setLogoUrl(dto.getLogoUrl());
        empresa.setSector(dto.getSector());
        empresa.setPais(dto.getPais());
        empresa.setDepartamento(dto.getDepartamento());
        empresa.setCiudad(dto.getCiudad());
        empresa.setCreador(creador);

        Empresa saved = empresaRepository.save(empresa);
        return convertToDTO(saved);
    }

    public EmpresaDTO update(Long id, EmpresaDTO dto) {
        Empresa empresa = empresaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empresa", id));

        empresa.setNombre(dto.getNombre());
        empresa.setDescripcion(dto.getDescripcion());
        empresa.setNit(dto.getNit());
        empresa.setCorreo(dto.getCorreo());
        empresa.setTelefono(dto.getTelefono());
        empresa.setDireccion(dto.getDireccion());
        empresa.setLogoUrl(dto.getLogoUrl());
        empresa.setSector(dto.getSector());
        empresa.setPais(dto.getPais());
        empresa.setDepartamento(dto.getDepartamento());
        empresa.setCiudad(dto.getCiudad());

        Empresa updated = empresaRepository.save(empresa);
        return convertToDTO(updated);
    }

    public void delete(Long id) {
        if (!empresaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Empresa", id);
        }
        List<Long> proyectoIds = proyectoRepository.findIdsByEmpresaId(id);
        for (Long proyectoId : proyectoIds) {
            comentarioTareaRepository.deleteByProyectoId(proyectoId);
            miembroProyectoRepository.deleteByProyectoId(proyectoId);
            tareaRepository.softDeleteByProyectoId(proyectoId);
        }
        proyectoRepository.softDeleteByEmpresaId(id);
        miembroEmpresaRepository.deleteByEmpresaId(id);
        empresaRepository.deleteById(id);
    }

    private EmpresaDTO convertToDTO(Empresa empresa) {
        EmpresaDTO dto = new EmpresaDTO();
        dto.setId(empresa.getId());
        dto.setNombre(empresa.getNombre());
        dto.setDescripcion(empresa.getDescripcion());
        dto.setNit(empresa.getNit());
        dto.setCorreo(empresa.getCorreo());
        dto.setTelefono(empresa.getTelefono());
        dto.setDireccion(empresa.getDireccion());
        dto.setLogoUrl(empresa.getLogoUrl());
        dto.setSector(empresa.getSector());
        if (empresa.getCreador() != null) {
            dto.setCreadorId(empresa.getCreador().getId());
            dto.setCreadorNombre(empresa.getCreador().getApodo() != null
                    ? empresa.getCreador().getApodo()
                    : empresa.getCreador().getNombre());
        }
        dto.setPais(empresa.getPais());
        dto.setDepartamento(empresa.getDepartamento());
        dto.setCiudad(empresa.getCiudad());
        dto.setFechaCreacion(empresa.getFechaCreacion());
        return dto;
    }
}
