package com.taskmanager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComentarioTareaDTO {
    private Long id;
    private String contenido;
    private Long tareaId;
    private Long autorId;
    private String autorNombre;
    private String autorApodo;
    private LocalDateTime fechaCreacion;
}
