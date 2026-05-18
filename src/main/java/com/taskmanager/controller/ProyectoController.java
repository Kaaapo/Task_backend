package com.taskmanager.controller;

import com.taskmanager.dto.ProyectoDTO;
import com.taskmanager.service.IProyectoService;
import com.taskmanager.service.MembershipPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/proyectos")
public class ProyectoController {

    @Autowired
    private IProyectoService proyectoService;

    @Autowired
    private MembershipPermissionService membershipPermissionService;

    @GetMapping
    public ResponseEntity<List<ProyectoDTO>> getAll(Authentication authentication) {
        List<ProyectoDTO> proyectos = proyectoService.findAccessibleForUser(authentication.getName());
        return ResponseEntity.ok(proyectos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProyectoDTO> getById(@PathVariable Long id, Authentication authentication) {
        membershipPermissionService.requireProyectoAccess(authentication.getName(), id);
        ProyectoDTO proyecto = proyectoService.findById(id);
        return ResponseEntity.ok(proyecto);
    }

    @GetMapping("/empresa/{empresaId}")
    public ResponseEntity<List<ProyectoDTO>> getByEmpresaId(
            @PathVariable Long empresaId,
            Authentication authentication) {
        membershipPermissionService.requireEmpresaAccess(authentication.getName(), empresaId);
        List<ProyectoDTO> proyectos = proyectoService.findAccessibleByEmpresaForUser(
                authentication.getName(), empresaId);
        return ResponseEntity.ok(proyectos);
    }

    @PostMapping
    public ResponseEntity<ProyectoDTO> create(@RequestBody ProyectoDTO dto, Authentication authentication) {
        ProyectoDTO created = proyectoService.create(dto, authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProyectoDTO> update(
            @PathVariable Long id,
            @RequestBody ProyectoDTO dto,
            Authentication authentication) {
        membershipPermissionService.requireProyectoManagement(authentication.getName(), id);
        ProyectoDTO updated = proyectoService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, Authentication authentication) {
        membershipPermissionService.requireProyectoManagement(authentication.getName(), id);
        proyectoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
