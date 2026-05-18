package com.taskmanager.controller;

import com.taskmanager.dto.ComentarioTareaDTO;
import com.taskmanager.service.IComentarioTareaService;
import com.taskmanager.service.MembershipPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tareas/{tareaId}/comentarios")
public class ComentarioTareaController {

    @Autowired
    private IComentarioTareaService comentarioTareaService;

    @Autowired
    private MembershipPermissionService membershipPermissionService;

    @GetMapping
    public ResponseEntity<List<ComentarioTareaDTO>> getComentarios(
            @PathVariable Long tareaId,
            Authentication authentication) {
        membershipPermissionService.requireTareaAccess(authentication.getName(), tareaId);
        List<ComentarioTareaDTO> comentarios = comentarioTareaService.findByTareaId(tareaId);
        return ResponseEntity.ok(comentarios);
    }

    @PostMapping
    public ResponseEntity<ComentarioTareaDTO> create(
            @PathVariable Long tareaId,
            @RequestBody ComentarioTareaDTO dto,
            Authentication authentication) {
        membershipPermissionService.requireTareaAccess(authentication.getName(), tareaId);
        ComentarioTareaDTO created = comentarioTareaService.create(tareaId, dto, authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ComentarioTareaDTO> update(
            @PathVariable Long tareaId,
            @PathVariable Long id,
            @RequestBody ComentarioTareaDTO dto,
            Authentication authentication) {
        membershipPermissionService.requireTareaAccess(authentication.getName(), tareaId);
        ComentarioTareaDTO updated = comentarioTareaService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long tareaId,
            @PathVariable Long id,
            Authentication authentication) {
        membershipPermissionService.requireTareaAccess(authentication.getName(), tareaId);
        comentarioTareaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
