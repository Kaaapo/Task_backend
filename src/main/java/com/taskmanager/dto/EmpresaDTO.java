package com.taskmanager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para Empresa
 * Incluye el nombre del estado para enriquecer la respuesta
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmpresaDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private String correo;
    private Long estadoId;
    private String estadoNombre; // Campo enriquecido
}
