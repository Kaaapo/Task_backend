package com.taskmanager.controller;

import com.taskmanager.dto.TipoProyectoDTO;
import com.taskmanager.service.TipoProyectoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller REST para Tipos de Proyecto
 * Endpoints: /api/tipos-proyecto
 */
@RestController
@RequestMapping("/api/tipos-proyecto")
@CrossOrigin(origins = "*")
public class TipoProyectoController {
    
    @Autowired
    private TipoProyectoService tipoProyectoService;
    
    @GetMapping
    public ResponseEntity<List<TipoProyectoDTO>> getAll() {
        List<TipoProyectoDTO> tipos = tipoProyectoService.findAll();
        return ResponseEntity.ok(tipos);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<TipoProyectoDTO> getById(@PathVariable Long id) {
        TipoProyectoDTO tipo = tipoProyectoService.findById(id);
        return ResponseEntity.ok(tipo);
    }
    
    @PostMapping
    public ResponseEntity<TipoProyectoDTO> create(@RequestBody TipoProyectoDTO dto) {
        TipoProyectoDTO created = tipoProyectoService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<TipoProyectoDTO> update(@PathVariable Long id, @RequestBody TipoProyectoDTO dto) {
        TipoProyectoDTO updated = tipoProyectoService.update(id, dto);
        return ResponseEntity.ok(updated);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        tipoProyectoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
