package com.taskmanager.repository;

import com.taskmanager.model.RefreshToken;
import com.taskmanager.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);

    @Modifying
    void deleteByUsuario(Usuario usuario);

    @Modifying
    void deleteByFechaExpiracionBefore(Instant fecha);
}
