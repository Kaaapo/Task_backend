package com.taskmanager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {
    private Long id;
    private String nombre;
    private String apellido;
    private String apodo;
    private String email;
    private String avatarUrl;
    private String telefono;
    private LocalDateTime fechaRegistro;
    private LocalDateTime ultimoAcceso;
    private Boolean activo;
}
