package com.taskmanager.service;

import com.taskmanager.exception.ResourceNotFoundException;
import com.taskmanager.model.Empresa;
import com.taskmanager.model.MiembroEmpresa;
import com.taskmanager.model.MiembroProyecto;
import com.taskmanager.model.Proyecto;
import com.taskmanager.model.Tarea;
import com.taskmanager.model.Usuario;
import com.taskmanager.repository.EmpresaRepository;
import com.taskmanager.repository.MiembroEmpresaRepository;
import com.taskmanager.repository.MiembroProyectoRepository;
import com.taskmanager.repository.ProyectoRepository;
import com.taskmanager.repository.TareaRepository;
import com.taskmanager.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class MembershipPermissionService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private ProyectoRepository proyectoRepository;

    @Autowired
    private MiembroEmpresaRepository miembroEmpresaRepository;

    @Autowired
    private MiembroProyectoRepository miembroProyectoRepository;

    @Autowired
    private TareaRepository tareaRepository;

    public Long requireUserId(String email) {
        Optional<Usuario> u = usuarioRepository.findByEmail(email);
        return u.map(Usuario::getId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no autenticado"));
    }

    public void requireEmpresaAccess(String email, Long empresaId) {
        Long uid = requireUserId(email);
        Empresa e = empresaRepository.findById(empresaId)
                .orElseThrow(() -> new ResourceNotFoundException("Empresa", empresaId));
        if (e.getCreador() != null && e.getCreador().getId().equals(uid)) {
            return;
        }
        MiembroEmpresa m = miembroEmpresaRepository.findByUsuarioIdAndEmpresaId(uid, empresaId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes acceso a esta empresa"));
        if (!Boolean.TRUE.equals(m.getActivo())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes acceso a esta empresa");
        }
    }

    public void requireEmpresaManagement(String email, Long empresaId) {
        Long uid = requireUserId(email);
        Empresa e = empresaRepository.findById(empresaId)
                .orElseThrow(() -> new ResourceNotFoundException("Empresa", empresaId));
        if (e.getCreador() != null && e.getCreador().getId().equals(uid)) {
            return;
        }
        MiembroEmpresa m = miembroEmpresaRepository.findByUsuarioIdAndEmpresaId(uid, empresaId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN,
                        "No puedes gestionar esta empresa"));
        if (!Boolean.TRUE.equals(m.getActivo()) || !"ADMIN".equalsIgnoreCase(m.getRol())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No puedes gestionar esta empresa");
        }
    }

    public void requireProyectoAccess(String email, Long proyectoId) {
        Long uid = requireUserId(email);
        Proyecto p = proyectoRepository.findById(proyectoId)
                .orElseThrow(() -> new ResourceNotFoundException("Proyecto", proyectoId));
        if (p.getCreador() != null && p.getCreador().getId().equals(uid)) {
            return;
        }
        if (miembroProyectoRepository.existsByUsuarioIdAndProyectoId(uid, proyectoId)) {
            return;
        }
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes acceso a este proyecto");
    }

    public void requireProyectoManagement(String email, Long proyectoId) {
        Long uid = requireUserId(email);
        Proyecto p = proyectoRepository.findById(proyectoId)
                .orElseThrow(() -> new ResourceNotFoundException("Proyecto", proyectoId));
        if (p.getCreador() != null && p.getCreador().getId().equals(uid)) {
            return;
        }
        Optional<MiembroProyecto> mpOpt = miembroProyectoRepository.findByUsuarioIdAndProyectoId(uid, proyectoId);
        if (mpOpt.isPresent() && "LIDER".equalsIgnoreCase(mpOpt.get().getRol())) {
            return;
        }
        try {
            requireEmpresaManagement(email, p.getEmpresa().getId());
        } catch (ResponseStatusException ex) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "No puedes gestionar el equipo de este proyecto");
        }
    }

    public void requireTareaAccess(String email, Long tareaId) {
        Tarea t = tareaRepository.findById(tareaId)
                .orElseThrow(() -> new ResourceNotFoundException("Tarea", tareaId));
        requireProyectoAccess(email, t.getProyecto().getId());
    }
}
