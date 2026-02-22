package com.taskmanager.controller;

import com.taskmanager.dto.FaseDTO;
import com.taskmanager.service.FaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller REST para Fases
 * Endpoints: /api/fases
 */
@RestController
@RequestMapping("/api/fases")
@CrossOrigin(origins = "*")
public class FaseController {
    
    @Autowired
    private FaseService faseService;
    
    @GetMapping
    public ResponseEntity<List<FaseDTO>> getAll() {
        List<FaseDTO> fases = faseService.findAll();
        return ResponseEntity.ok(fases);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<FaseDTO> getById(@PathVariable Long id) {
        FaseDTO fase = faseService.findById(id);
        return ResponseEntity.ok(fase);
    }
    
    @PostMapping
    public ResponseEntity<FaseDTO> create(@RequestBody FaseDTO dto) {
        FaseDTO created = faseService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<FaseDTO> update(@PathVariable Long id, @RequestBody FaseDTO dto) {
        FaseDTO updated = faseService.update(id, dto);
        return ResponseEntity.ok(updated);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        faseService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
