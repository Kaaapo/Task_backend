package com.taskmanager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MiembroProyectoDTO {
    private Long id;
    private Long usuarioId;
    private String usuarioNombre;
    private String usuarioApodo;
    private String usuarioEmail;
    private Long proyectoId;
    private String proyectoNombre;
    private String rol;
    private LocalDateTime fechaAsignacion;
}
