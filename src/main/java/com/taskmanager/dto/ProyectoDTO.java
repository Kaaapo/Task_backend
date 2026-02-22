package com.taskmanager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para Proyecto
 * Incluye nombres enriquecidos de empresa, tipo y estado
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProyectoDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private Long empresaId;
    private String empresaNombre; // Campo enriquecido
    private Long tipoProyectoId;
    private String tipoProyectoNombre; // Campo enriquecido
    private Long estadoId;
    private String estadoNombre; // Campo enriquecido
}
