package com.taskmanager.controller;

import com.taskmanager.dto.UsuarioDTO;
import com.taskmanager.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/me")
    public ResponseEntity<UsuarioDTO> getMe(Authentication authentication) {
        UsuarioDTO usuario = usuarioService.findByEmail(authentication.getName());
        return ResponseEntity.ok(usuario);
    }

    @PutMapping("/me")
    public ResponseEntity<UsuarioDTO> updateMe(Authentication authentication, @RequestBody UsuarioDTO dto) {
        UsuarioDTO current = usuarioService.findByEmail(authentication.getName());
        UsuarioDTO updated = usuarioService.update(current.getId(), dto);
        return ResponseEntity.ok(updated);
    }
}
