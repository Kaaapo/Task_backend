package com.taskmanager.controller;

import com.taskmanager.dto.SistemaDTO;
import com.taskmanager.service.SistemaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller REST para Sistemas
 * Endpoints: /api/sistemas
 */
@RestController
@RequestMapping("/api/sistemas")
@CrossOrigin(origins = "*")
public class SistemaController {
    
    @Autowired
    private SistemaService sistemaService;
    
    @GetMapping
    public ResponseEntity<List<SistemaDTO>> getAll() {
        List<SistemaDTO> sistemas = sistemaService.findAll();
        return ResponseEntity.ok(sistemas);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<SistemaDTO> getById(@PathVariable Long id) {
        SistemaDTO sistema = sistemaService.findById(id);
        return ResponseEntity.ok(sistema);
    }
    
    @PostMapping
    public ResponseEntity<SistemaDTO> create(@RequestBody SistemaDTO dto) {
        SistemaDTO created = sistemaService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<SistemaDTO> update(@PathVariable Long id, @RequestBody SistemaDTO dto) {
        SistemaDTO updated = sistemaService.update(id, dto);
        return ResponseEntity.ok(updated);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        sistemaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
