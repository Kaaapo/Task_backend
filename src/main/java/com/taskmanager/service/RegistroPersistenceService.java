package com.taskmanager.service;

import com.taskmanager.dto.RegistroRequest;
import com.taskmanager.model.Usuario;
import com.taskmanager.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Persiste el usuario en una transacción propia para poder enviar el correo SMTP después,
 * sin envolver el envío en la misma transacción ni en un hilo en segundo plano (donde los fallos pasaban desapercibidos).
 */
@Service
public class RegistroPersistenceService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Usuario guardarUsuarioNuevo(RegistroRequest request, String tokenVerificacion, LocalDateTime tokenExpiracion) {
        Usuario usuario = new Usuario();
        usuario.setNombre(request.getNombre());
        usuario.setApellido(request.getApellido());
        usuario.setApodo(request.getApodo() != null ? request.getApodo() : request.getNombre());
        usuario.setEmail(request.getEmail());
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        usuario.setTelefono(request.getTelefono());
        usuario.setActivo(true);
        usuario.setEmailVerificado(false);
        usuario.setTokenVerificacion(tokenVerificacion);
        usuario.setTokenVerificacionExpiracion(tokenExpiracion);
        return usuarioRepository.save(usuario);
    }
}
