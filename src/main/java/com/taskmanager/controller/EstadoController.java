package com.taskmanager.controller;

import com.taskmanager.dto.EstadoDTO;
import com.taskmanager.service.EstadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller REST para Estados
 * Endpoints: /api/estados
 */
@RestController
@RequestMapping("/api/estados")
@CrossOrigin(origins = "*")
public class EstadoController {
    
    @Autowired
    private EstadoService estadoService;
    
    /**
     * GET /api/estados
     * Obtener todos los estados
     */
    @GetMapping
    public ResponseEntity<List<EstadoDTO>> getAll() {
        List<EstadoDTO> estados = estadoService.findAll();
        return ResponseEntity.ok(estados);
    }
    
    /**
     * GET /api/estados/{id}
     * Obtener estado por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<EstadoDTO> getById(@PathVariable Long id) {
        EstadoDTO estado = estadoService.findById(id);
        return ResponseEntity.ok(estado);
    }
    
    /**
     * POST /api/estados
     * Crear nuevo estado
     */
    @PostMapping
    public ResponseEntity<EstadoDTO> create(@RequestBody EstadoDTO dto) {
        EstadoDTO created = estadoService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    
    /**
     * PUT /api/estados/{id}
     * Actualizar estado existente
     */
    @PutMapping("/{id}")
    public ResponseEntity<EstadoDTO> update(@PathVariable Long id, @RequestBody EstadoDTO dto) {
        EstadoDTO updated = estadoService.update(id, dto);
        return ResponseEntity.ok(updated);
    }
    
    /**
     * DELETE /api/estados/{id}
     * Eliminar estado
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        estadoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
