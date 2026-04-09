package com.taskmanager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TareaDTO {
    private Long id;
    private String titulo;
    private String descripcion;
    private Long proyectoId;
    private String proyectoNombre;
    private Long estadoId;
    private String estadoNombre;
    private Long asignadoId;
    private String asignadoNombre;
    private Long creadorId;
    private String creadorNombre;
    private String prioridad;
    private LocalDate fechaLimite;
    private LocalDate fechaCompletada;
    private Integer orden;
    private LocalDateTime fechaCreacion;
    private List<Long> etiquetaIds;
}
