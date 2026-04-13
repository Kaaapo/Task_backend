package com.taskmanager.repository;

import com.taskmanager.model.TokenRecuperacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface TokenRecuperacionRepository extends JpaRepository<TokenRecuperacion, Long> {
    Optional<TokenRecuperacion> findByTokenAndUsadoFalse(String token);

    @Modifying
    void deleteByFechaExpiracionBefore(LocalDateTime fecha);
}
