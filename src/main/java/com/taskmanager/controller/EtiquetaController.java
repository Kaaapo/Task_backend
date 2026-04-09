package com.taskmanager.controller;

import com.taskmanager.dto.EtiquetaDTO;
import com.taskmanager.service.EtiquetaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/etiquetas")
public class EtiquetaController {

    @Autowired
    private EtiquetaService etiquetaService;

    @GetMapping
    public ResponseEntity<List<EtiquetaDTO>> getAll() {
        List<EtiquetaDTO> etiquetas = etiquetaService.findAll();
        return ResponseEntity.ok(etiquetas);
    }

    @GetMapping("/empresa/{empresaId}")
    public ResponseEntity<List<EtiquetaDTO>> getByEmpresaId(@PathVariable Long empresaId) {
        List<EtiquetaDTO> etiquetas = etiquetaService.findByEmpresaId(empresaId);
        return ResponseEntity.ok(etiquetas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EtiquetaDTO> getById(@PathVariable Long id) {
        EtiquetaDTO etiqueta = etiquetaService.findById(id);
        return ResponseEntity.ok(etiqueta);
    }

    @PostMapping
    public ResponseEntity<EtiquetaDTO> create(@RequestBody EtiquetaDTO dto) {
        EtiquetaDTO created = etiquetaService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EtiquetaDTO> update(@PathVariable Long id, @RequestBody EtiquetaDTO dto) {
        EtiquetaDTO updated = etiquetaService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        etiquetaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
