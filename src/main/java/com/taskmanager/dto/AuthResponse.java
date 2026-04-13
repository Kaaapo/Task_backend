package com.taskmanager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private String tipo = "Bearer";
    private String refreshToken;
    private Long id;
    private String nombre;
    private String apellido;
    private String apodo;
    private String email;
    private Boolean emailVerificado;

    public AuthResponse(String token, String refreshToken, Long id, String nombre,
                        String apellido, String apodo, String email, Boolean emailVerificado) {
        this.token = token;
        this.refreshToken = refreshToken;
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.apodo = apodo;
        this.email = email;
        this.emailVerificado = emailVerificado;
    }
}
