package com.taskmanager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EtiquetaDTO {
    private Long id;
    private String nombre;
    private String color;
    private Long empresaId;
    private String empresaNombre;
}
