package com.taskmanager.controller;

import com.taskmanager.dto.*;
import com.taskmanager.service.AuthService;
import com.taskmanager.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/registro")
    public ResponseEntity<MensajeResponse> registro(@Valid @RequestBody RegistroRequest request) {
        MensajeResponse response = authService.registro(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/verificar-email")
    public ResponseEntity<MensajeResponse> verificarEmail(@RequestParam String token) {
        MensajeResponse response = authService.verificarEmail(token);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/reenviar-verificacion")
    public ResponseEntity<MensajeResponse> reenviarVerificacion(@Valid @RequestBody SolicitarResetRequest request) {
        MensajeResponse response = authService.reenviarVerificacion(request.getEmail());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/solicitar-reset")
    public ResponseEntity<MensajeResponse> solicitarReset(@Valid @RequestBody SolicitarResetRequest request) {
        MensajeResponse response = authService.solicitarResetPassword(request.getEmail());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<MensajeResponse> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        MensajeResponse response = authService.resetPassword(request.getToken(), request.getNuevaPassword());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        AuthResponse response = authService.refreshToken(request.getRefreshToken());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<MensajeResponse> logout(@Valid @RequestBody RefreshTokenRequest request) {
        MensajeResponse response = authService.logout(request.getRefreshToken());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/perfil")
    public ResponseEntity<UsuarioDTO> getPerfil(Authentication authentication) {
        UsuarioDTO usuario = usuarioService.findByEmail(authentication.getName());
        return ResponseEntity.ok(usuario);
    }
}
