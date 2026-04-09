package com.taskmanager.controller;

import com.taskmanager.dto.MiembroProyectoDTO;
import com.taskmanager.service.MiembroProyectoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/proyectos/{proyectoId}/miembros")
public class MiembroProyectoController {

    @Autowired
    private MiembroProyectoService miembroProyectoService;

    @GetMapping
    public ResponseEntity<List<MiembroProyectoDTO>> getMiembros(@PathVariable Long proyectoId) {
        List<MiembroProyectoDTO> miembros = miembroProyectoService.findByProyectoId(proyectoId);
        return ResponseEntity.ok(miembros);
    }

    @PostMapping
    public ResponseEntity<MiembroProyectoDTO> addMiembro(
            @PathVariable Long proyectoId,
            @RequestBody Map<String, Object> body) {
        Long usuarioId = Long.valueOf(body.get("usuarioId").toString());
        String rol = body.containsKey("rol") ? body.get("rol").toString() : "DESARROLLADOR";
        MiembroProyectoDTO created = miembroProyectoService.addMiembro(proyectoId, usuarioId, rol);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MiembroProyectoDTO> updateRol(
            @PathVariable Long proyectoId,
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        MiembroProyectoDTO updated = miembroProyectoService.updateRol(id, body.get("rol"));
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeMiembro(@PathVariable Long proyectoId, @PathVariable Long id) {
        miembroProyectoService.removeMiembro(id);
        return ResponseEntity.noContent().build();
    }
}
