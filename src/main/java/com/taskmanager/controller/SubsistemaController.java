package com.taskmanager.controller;

import com.taskmanager.dto.SubsistemaDTO;
import com.taskmanager.service.SubsistemaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller REST para Subsistemas
 * Endpoints: /api/subsistemas
 */
@RestController
@RequestMapping("/api/subsistemas")
@CrossOrigin(origins = "*")
public class SubsistemaController {
    
    @Autowired
    private SubsistemaService subsistemaService;
    
    @GetMapping
    public ResponseEntity<List<SubsistemaDTO>> getAll() {
        List<SubsistemaDTO> subsistemas = subsistemaService.findAll();
        return ResponseEntity.ok(subsistemas);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<SubsistemaDTO> getById(@PathVariable Long id) {
        SubsistemaDTO subsistema = subsistemaService.findById(id);
        return ResponseEntity.ok(subsistema);
    }
    
    @PostMapping
    public ResponseEntity<SubsistemaDTO> create(@RequestBody SubsistemaDTO dto) {
        SubsistemaDTO created = subsistemaService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<SubsistemaDTO> update(@PathVariable Long id, @RequestBody SubsistemaDTO dto) {
        SubsistemaDTO updated = subsistemaService.update(id, dto);
        return ResponseEntity.ok(updated);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        subsistemaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
