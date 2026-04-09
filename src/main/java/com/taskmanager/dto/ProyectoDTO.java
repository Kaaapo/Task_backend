package com.taskmanager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProyectoDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private String codigo;
    private Long empresaId;
    private String empresaNombre;
    private Long tipoProyectoId;
    private String tipoProyectoNombre;
    private Long estadoId;
    private String estadoNombre;
    private Long creadorId;
    private String creadorNombre;
    private String prioridad;
    private LocalDate fechaInicio;
    private LocalDate fechaFinEstimada;
    private LocalDate fechaFinReal;
    private BigDecimal progreso;
    private LocalDateTime fechaCreacion;
}
