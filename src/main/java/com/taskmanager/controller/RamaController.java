package com.taskmanager.controller;

import com.taskmanager.dto.RamaDTO;
import com.taskmanager.service.RamaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller REST para Ramas
 * Endpoints: /api/ramas
 */
@RestController
@RequestMapping("/api/ramas")
@CrossOrigin(origins = "*")
public class RamaController {
    
    @Autowired
    private RamaService ramaService;
    
    @GetMapping
    public ResponseEntity<List<RamaDTO>> getAll() {
        List<RamaDTO> ramas = ramaService.findAll();
        return ResponseEntity.ok(ramas);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<RamaDTO> getById(@PathVariable Long id) {
        RamaDTO rama = ramaService.findById(id);
        return ResponseEntity.ok(rama);
    }
    
    @PostMapping
    public ResponseEntity<RamaDTO> create(@RequestBody RamaDTO dto) {
        RamaDTO created = ramaService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<RamaDTO> update(@PathVariable Long id, @RequestBody RamaDTO dto) {
        RamaDTO updated = ramaService.update(id, dto);
        return ResponseEntity.ok(updated);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        ramaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
