package com.taskmanager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para Sistema
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SistemaDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private Long estadoId;
    private String estadoNombre; // Campo enriquecido
}
