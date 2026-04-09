package com.taskmanager.controller;

import com.taskmanager.dto.TareaDTO;
import com.taskmanager.service.TareaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tareas")
public class TareaController {

    @Autowired
    private TareaService tareaService;

    @GetMapping
    public ResponseEntity<List<TareaDTO>> getAll() {
        List<TareaDTO> tareas = tareaService.findAll();
        return ResponseEntity.ok(tareas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TareaDTO> getById(@PathVariable Long id) {
        TareaDTO tarea = tareaService.findById(id);
        return ResponseEntity.ok(tarea);
    }

    @GetMapping("/proyecto/{proyectoId}")
    public ResponseEntity<List<TareaDTO>> getByProyectoId(@PathVariable Long proyectoId) {
        List<TareaDTO> tareas = tareaService.findByProyectoId(proyectoId);
        return ResponseEntity.ok(tareas);
    }

    @GetMapping("/asignado/{asignadoId}")
    public ResponseEntity<List<TareaDTO>> getByAsignadoId(@PathVariable Long asignadoId) {
        List<TareaDTO> tareas = tareaService.findByAsignadoId(asignadoId);
        return ResponseEntity.ok(tareas);
    }

    @GetMapping("/proyecto/{proyectoId}/estado/{estadoId}")
    public ResponseEntity<List<TareaDTO>> getByProyectoAndEstado(
            @PathVariable Long proyectoId,
            @PathVariable Long estadoId) {
        List<TareaDTO> tareas = tareaService.findByProyectoIdAndEstadoId(proyectoId, estadoId);
        return ResponseEntity.ok(tareas);
    }

    @PostMapping
    public ResponseEntity<TareaDTO> create(@RequestBody TareaDTO dto, Authentication authentication) {
        TareaDTO created = tareaService.create(dto, authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TareaDTO> update(@PathVariable Long id, @RequestBody TareaDTO dto) {
        TareaDTO updated = tareaService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        tareaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
