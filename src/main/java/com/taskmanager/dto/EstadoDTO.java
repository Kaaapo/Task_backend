package com.taskmanager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para Estado
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstadoDTO {
    private Long id;
    private String nombre;
    private String descripcion;
}
