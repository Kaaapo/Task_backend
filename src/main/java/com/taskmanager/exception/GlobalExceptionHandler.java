package com.taskmanager.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleResourceNotFoundException(
            ResourceNotFoundException ex, WebRequest request) {

        Map<String, Object> body = buildBody(HttpStatus.NOT_FOUND, "Not Found", ex.getMessage(), request);
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String, Object>> handleBadCredentials(
            BadCredentialsException ex, WebRequest request) {

        Map<String, Object> body = buildBody(HttpStatus.UNAUTHORIZED, "Unauthorized",
                ex.getMessage(), request);
        body.put("code", "BAD_CREDENTIALS");
        return new ResponseEntity<>(body, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Map<String, Object>> handleAuthenticationException(
            AuthenticationException ex, WebRequest request) {

        Map<String, Object> body = buildBody(HttpStatus.UNAUTHORIZED, "Unauthorized",
                "Error de autenticación: " + ex.getMessage(), request);
        return new ResponseEntity<>(body, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, Object>> handleAccessDeniedException(
            AccessDeniedException ex, WebRequest request) {

        Map<String, Object> body = buildBody(HttpStatus.FORBIDDEN, "Forbidden",
                "No tienes permisos para acceder a este recurso", request);
        return new ResponseEntity<>(body, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(EmailNoVerificadoException.class)
    public ResponseEntity<Map<String, Object>> handleEmailNoVerificado(
            EmailNoVerificadoException ex, WebRequest request) {

        Map<String, Object> body = buildBody(HttpStatus.FORBIDDEN, "Forbidden",
                ex.getMessage(), request);
        body.put("code", "EMAIL_NOT_VERIFIED");
        return new ResponseEntity<>(body, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(TokenExpiradoException.class)
    public ResponseEntity<Map<String, Object>> handleTokenExpirado(
            TokenExpiradoException ex, WebRequest request) {

        Map<String, Object> body = buildBody(HttpStatus.BAD_REQUEST, "Bad Request",
                ex.getMessage(), request);
        body.put("code", "TOKEN_EXPIRED");
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CuentaBloqueadaException.class)
    public ResponseEntity<Map<String, Object>> handleCuentaBloqueada(
            CuentaBloqueadaException ex, WebRequest request) {

        Map<String, Object> body = buildBody(HttpStatus.LOCKED, "Locked",
                ex.getMessage(), request);
        body.put("code", "ACCOUNT_LOCKED");
        return new ResponseEntity<>(body, HttpStatus.LOCKED);
    }

    @ExceptionHandler(RateLimitExceededException.class)
    public ResponseEntity<Map<String, Object>> handleRateLimitExceeded(
            RateLimitExceededException ex, WebRequest request) {

        Map<String, Object> body = buildBody(HttpStatus.TOO_MANY_REQUESTS, "Too Many Requests",
                ex.getMessage(), request);
        body.put("code", "RATE_LIMIT_EXCEEDED");
        return new ResponseEntity<>(body, HttpStatus.TOO_MANY_REQUESTS);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(
            MethodArgumentNotValidException ex, WebRequest request) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));

        String firstError = errors.values().stream().findFirst().orElse("Error de validación");
        Map<String, Object> body = buildBody(HttpStatus.BAD_REQUEST, "Bad Request",
                firstError, request);
        body.put("code", "VALIDATION_ERROR");
        body.put("errors", errors);
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(
            RuntimeException ex, WebRequest request) {

        Map<String, Object> body = buildBody(HttpStatus.BAD_REQUEST, "Bad Request",
                ex.getMessage(), request);
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGlobalException(
            Exception ex, WebRequest request) {

        Map<String, Object> body = buildBody(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error",
                ex.getMessage(), request);
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private Map<String, Object> buildBody(HttpStatus status, String error, String message, WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", error);
        body.put("message", message);
        body.put("path", request.getDescription(false).replace("uri=", ""));
        return body;
    }
}
