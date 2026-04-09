package com.taskmanager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MiembroEmpresaDTO {
    private Long id;
    private Long usuarioId;
    private String usuarioNombre;
    private String usuarioApodo;
    private String usuarioEmail;
    private Long empresaId;
    private String empresaNombre;
    private String rol;
    private LocalDateTime fechaUnion;
    private Boolean activo;
}
