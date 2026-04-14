package com.taskmanager.service;

import com.taskmanager.exception.TokenExpiradoException;
import com.taskmanager.model.RefreshToken;
import com.taskmanager.model.Usuario;
import com.taskmanager.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@Transactional
public class RefreshTokenService {

    @Value("${jwt.refresh-expiration}")
    private long refreshExpiration;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    public RefreshToken crearRefreshToken(Usuario usuario) {
        refreshTokenRepository.deleteByUsuario(usuario);

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUsuario(usuario);
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setFechaExpiracion(Instant.now().plusMillis(refreshExpiration));

        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken verificarRefreshToken(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new TokenExpiradoException("Tu sesión no es válida. Por favor, inicia sesión nuevamente."));

        if (refreshToken.getFechaExpiracion().isBefore(Instant.now())) {
            refreshTokenRepository.delete(refreshToken);
            throw new TokenExpiradoException("Tu sesión ha expirado. Por favor, inicia sesión nuevamente.");
        }

        return refreshToken;
    }

    public void eliminarPorUsuario(Usuario usuario) {
        refreshTokenRepository.deleteByUsuario(usuario);
    }

    public void limpiarTokensExpirados() {
        refreshTokenRepository.deleteByFechaExpiracionBefore(Instant.now());
    }
}
