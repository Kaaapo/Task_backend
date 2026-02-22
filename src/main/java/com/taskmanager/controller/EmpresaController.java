package com.taskmanager.controller;

import com.taskmanager.dto.EmpresaDTO;
import com.taskmanager.service.EmpresaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller REST para Empresas
 * Endpoints: /api/empresas
 */
@RestController
@RequestMapping("/api/empresas")
@CrossOrigin(origins = "*")
public class EmpresaController {
    
    @Autowired
    private EmpresaService empresaService;
    
    /**
     * GET /api/empresas
     * Obtener todas las empresas
     */
    @GetMapping
    public ResponseEntity<List<EmpresaDTO>> getAll() {
        List<EmpresaDTO> empresas = empresaService.findAll();
        return ResponseEntity.ok(empresas);
    }
    
    /**
     * GET /api/empresas/{id}
     * Obtener empresa por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<EmpresaDTO> getById(@PathVariable Long id) {
        EmpresaDTO empresa = empresaService.findById(id);
        return ResponseEntity.ok(empresa);
    }
    
    /**
     * POST /api/empresas
     * Crear nueva empresa
     */
    @PostMapping
    public ResponseEntity<EmpresaDTO> create(@RequestBody EmpresaDTO dto) {
        EmpresaDTO created = empresaService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    
    /**
     * PUT /api/empresas/{id}
     * Actualizar empresa existente
     */
    @PutMapping("/{id}")
    public ResponseEntity<EmpresaDTO> update(@PathVariable Long id, @RequestBody EmpresaDTO dto) {
        EmpresaDTO updated = empresaService.update(id, dto);
        return ResponseEntity.ok(updated);
    }
    
    /**
     * DELETE /api/empresas/{id}
     * Eliminar empresa
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        empresaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
