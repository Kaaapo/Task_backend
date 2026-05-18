package com.taskmanager.controller;

import com.taskmanager.dto.UsuarioDTO;
import com.taskmanager.service.IUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private IUsuarioService usuarioService;

    @GetMapping("/buscar")
    public ResponseEntity<List<UsuarioDTO>> buscar(
            @RequestParam(value = "q", required = false, defaultValue = "") String q) {
        String trimmed = q != null ? q.trim() : "";
        if (trimmed.length() < 2) {
            return ResponseEntity.ok(Collections.emptyList());
        }
        return ResponseEntity.ok(usuarioService.buscarParaSelector(trimmed));
    }

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
