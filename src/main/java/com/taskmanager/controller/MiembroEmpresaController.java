package com.taskmanager.controller;

import com.taskmanager.dto.MiembroEmpresaDTO;
import com.taskmanager.service.MiembroEmpresaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/empresas/{empresaId}/miembros")
public class MiembroEmpresaController {

    @Autowired
    private MiembroEmpresaService miembroEmpresaService;

    @GetMapping
    public ResponseEntity<List<MiembroEmpresaDTO>> getMiembros(@PathVariable Long empresaId) {
        List<MiembroEmpresaDTO> miembros = miembroEmpresaService.findByEmpresaId(empresaId);
        return ResponseEntity.ok(miembros);
    }

    @PostMapping
    public ResponseEntity<MiembroEmpresaDTO> addMiembro(
            @PathVariable Long empresaId,
            @RequestBody Map<String, Object> body) {
        Long usuarioId = Long.valueOf(body.get("usuarioId").toString());
        String rol = body.containsKey("rol") ? body.get("rol").toString() : "MIEMBRO";
        MiembroEmpresaDTO created = miembroEmpresaService.addMiembro(empresaId, usuarioId, rol);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MiembroEmpresaDTO> updateRol(
            @PathVariable Long empresaId,
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        MiembroEmpresaDTO updated = miembroEmpresaService.updateRol(id, body.get("rol"));
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeMiembro(@PathVariable Long empresaId, @PathVariable Long id) {
        miembroEmpresaService.removeMiembro(id);
        return ResponseEntity.noContent().build();
    }
}
