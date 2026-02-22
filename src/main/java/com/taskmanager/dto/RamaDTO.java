package com.taskmanager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para Rama
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RamaDTO {
    private Long id;
    private String nombre;
    private String descripcion;
}
