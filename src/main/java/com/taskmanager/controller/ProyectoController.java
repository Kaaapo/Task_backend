package com.taskmanager.controller;

import com.taskmanager.dto.ProyectoDTO;
import com.taskmanager.service.ProyectoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller REST para Proyectos
 * Endpoints: /api/proyectos
 */
@RestController
@RequestMapping("/api/proyectos")
@CrossOrigin(origins = "*")
public class ProyectoController {
    
    @Autowired
    private ProyectoService proyectoService;
    
    @GetMapping
    public ResponseEntity<List<ProyectoDTO>> getAll() {
        List<ProyectoDTO> proyectos = proyectoService.findAll();
        return ResponseEntity.ok(proyectos);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ProyectoDTO> getById(@PathVariable Long id) {
        ProyectoDTO proyecto = proyectoService.findById(id);
        return ResponseEntity.ok(proyecto);
    }
    
    @PostMapping
    public ResponseEntity<ProyectoDTO> create(@RequestBody ProyectoDTO dto) {
        ProyectoDTO created = proyectoService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ProyectoDTO> update(@PathVariable Long id, @RequestBody ProyectoDTO dto) {
        ProyectoDTO updated = proyectoService.update(id, dto);
        return ResponseEntity.ok(updated);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        proyectoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
