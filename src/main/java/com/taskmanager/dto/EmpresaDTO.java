package com.taskmanager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmpresaDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private String nit;
    private String correo;
    private String telefono;
    private String direccion;
    private String logoUrl;
    private String sector;
    private Long creadorId;
    private String creadorNombre;
    private Long estadoId;
    private String estadoNombre;
    private LocalDateTime fechaCreacion;
}
