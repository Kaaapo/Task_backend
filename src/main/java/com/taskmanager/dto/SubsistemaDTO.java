package com.taskmanager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para Subsistema
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubsistemaDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private Long sistemaId;
    private String sistemaNombre; // Campo enriquecido
    private Long estadoId;
    private String estadoNombre; // Campo enriquecido
}
