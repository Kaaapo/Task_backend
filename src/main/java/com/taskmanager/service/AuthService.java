package com.taskmanager.service;

import com.taskmanager.dto.AuthResponse;
import com.taskmanager.dto.LoginRequest;
import com.taskmanager.dto.MensajeResponse;
import com.taskmanager.dto.RegistroRequest;
import com.taskmanager.exception.CuentaBloqueadaException;
import com.taskmanager.exception.EmailNoVerificadoException;
import com.taskmanager.exception.ResourceNotFoundException;
import com.taskmanager.exception.TokenExpiradoException;
import com.taskmanager.model.RefreshToken;
import com.taskmanager.model.TokenRecuperacion;
import com.taskmanager.model.Usuario;
import com.taskmanager.repository.TokenRecuperacionRepository;
import com.taskmanager.repository.UsuarioRepository;
import com.taskmanager.security.JwtTokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
@Transactional
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    /** Mín. 8, mayúscula, minúscula, número y al menos un símbolo (cualquier carácter que no sea letra ni dígito). Sin espacios. */
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9])\\S{8,128}$"
    );

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TokenRecuperacionRepository tokenRecuperacionRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private EmailService emailService;

    @Autowired
    private RegistroPersistenceService registroPersistenceService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Value("${security.max-login-attempts}")
    private int maxLoginAttempts;

    @Value("${security.lockout-duration-minutes}")
    private int lockoutDurationMinutes;

    public MensajeResponse registro(RegistroRequest request) {
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Ya existe una cuenta registrada con este correo electrónico.");
        }

        validarFortalezaPassword(request.getPassword());

        String tokenVerificacion = UUID.randomUUID().toString();
        LocalDateTime expiracion = LocalDateTime.now().plusHours(24);
        Usuario usuario = registroPersistenceService.guardarUsuarioNuevo(request, tokenVerificacion, expiracion);

        String email = usuario.getEmail();
        String nombre = usuario.getNombre();
        log.info("Enviando correo de verificación a {}", email);
        try {
            emailService.enviarVerificacionEmail(email, nombre, tokenVerificacion);
            return new MensajeResponse(
                    "Registro exitoso. Revisa tu correo electrónico para verificar tu cuenta.",
                    true);
        } catch (Exception e) {
            log.error("Error enviando email de verificación a {}: {}", email, e.getMessage(), e);
            return new MensajeResponse(
                    "Tu cuenta se creó correctamente, pero no pudimos enviar el correo de verificación. "
                            + "Configura MAIL_USERNAME y MAIL_PASSWORD (SMTP) en el servidor, o usa «Reenviar verificación» en el inicio de sesión.",
                    false);
        }
    }

    public AuthResponse login(LoginRequest request) {
        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadCredentialsException(
                        "El correo electrónico o la contraseña son incorrectos. Verifica tus datos e intenta de nuevo."));

        verificarBloqueo(usuario);

        if (!usuario.getEmailVerificado()) {
            throw new EmailNoVerificadoException(
                    "Debes verificar tu correo electrónico antes de iniciar sesión. Revisa tu bandeja de entrada o solicita un nuevo enlace de verificación.");
        }

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            usuario.setIntentosFallidos(0);
            usuario.setCuentaBloqueadaHasta(null);
            usuario.setUltimoAcceso(LocalDateTime.now());
            usuarioRepository.save(usuario);

            String accessToken = jwtTokenProvider.generateToken(authentication);
            RefreshToken refreshToken = refreshTokenService.crearRefreshToken(usuario);

            return new AuthResponse(accessToken, refreshToken.getToken(), usuario.getId(),
                    usuario.getNombre(), usuario.getApellido(), usuario.getApodo(),
                    usuario.getEmail(), usuario.getEmailVerificado());

        } catch (BadCredentialsException e) {
            manejarLoginFallido(usuario);
            throw new BadCredentialsException(buildLoginFailedMessage(usuario));
        }
    }

    public MensajeResponse verificarEmail(String token) {
        Usuario usuario = usuarioRepository.findByTokenVerificacion(token)
                .orElseThrow(() -> new TokenExpiradoException("El enlace de verificación no es válido. Solicita uno nuevo desde la página de inicio de sesión."));

        if (usuario.getTokenVerificacionExpiracion().isBefore(LocalDateTime.now())) {
            throw new TokenExpiradoException("El token de verificación ha expirado. Solicita uno nuevo.");
        }

        usuario.setEmailVerificado(true);
        usuario.setTokenVerificacion(null);
        usuario.setTokenVerificacionExpiracion(null);
        usuarioRepository.save(usuario);

        return new MensajeResponse("Correo electrónico verificado exitosamente. Ya puedes iniciar sesión.");
    }

    public MensajeResponse reenviarVerificacion(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("No se encontró una cuenta con ese correo electrónico."));

        if (usuario.getEmailVerificado()) {
            throw new RuntimeException("Este correo electrónico ya está verificado. Puedes iniciar sesión directamente.");
        }

        String tokenVerificacion = UUID.randomUUID().toString();
        usuario.setTokenVerificacion(tokenVerificacion);
        usuario.setTokenVerificacionExpiracion(LocalDateTime.now().plusHours(24));
        usuarioRepository.save(usuario);

        String emailRv = usuario.getEmail();
        String nombreRv = usuario.getNombre();
        try {
            emailService.enviarVerificacionEmail(emailRv, nombreRv, tokenVerificacion);
            return new MensajeResponse(
                    "Se ha enviado un nuevo correo de verificación. Revisa tu bandeja de entrada.",
                    true);
        } catch (Exception e) {
            log.error("Error reenviando verificación a {}: {}", emailRv, e.getMessage(), e);
            return new MensajeResponse(
                    "No pudimos enviar el correo. Comprueba MAIL_USERNAME y MAIL_PASSWORD (SMTP) en el servidor o intenta más tarde.",
                    false);
        }
    }

    public MensajeResponse solicitarResetPassword(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No hay ninguna cuenta registrada con este correo electrónico. Verifica el correo o regístrate."));

        String token = UUID.randomUUID().toString();
        TokenRecuperacion tokenRecuperacion = new TokenRecuperacion();
        tokenRecuperacion.setToken(token);
        tokenRecuperacion.setUsuario(usuario);
        tokenRecuperacion.setFechaExpiracion(LocalDateTime.now().plusHours(1));
        tokenRecuperacion.setUsado(false);
        tokenRecuperacionRepository.save(tokenRecuperacion);

        String emailRs = usuario.getEmail();
        String nombreRs = usuario.getNombre();
        try {
            emailService.enviarRecuperacionPassword(emailRs, nombreRs, token);
            return new MensajeResponse(
                    "Te hemos enviado un correo con un enlace para restablecer tu contraseña. Revisa tu bandeja de entrada y la carpeta de spam.",
                    true);
        } catch (Exception e) {
            log.error("Error enviando email de recuperación a {}: {}", emailRs, e.getMessage(), e);
            return new MensajeResponse(
                    "Registramos tu solicitud, pero no pudimos enviar el correo. Configura SMTP (MAIL_USERNAME / MAIL_PASSWORD) o intenta más tarde.",
                    false);
        }
    }

    public MensajeResponse resetPassword(String token, String nuevaPassword) {
        validarFortalezaPassword(nuevaPassword);

        TokenRecuperacion tokenRecuperacion = tokenRecuperacionRepository.findByTokenAndUsadoFalse(token)
                .orElseThrow(() -> new TokenExpiradoException("El enlace de recuperación no es válido o ya fue utilizado. Solicita uno nuevo."));

        if (tokenRecuperacion.getFechaExpiracion().isBefore(LocalDateTime.now())) {
            throw new TokenExpiradoException("El token de recuperación ha expirado. Solicita uno nuevo.");
        }

        Usuario usuario = tokenRecuperacion.getUsuario();
        usuario.setPassword(passwordEncoder.encode(nuevaPassword));
        usuario.setIntentosFallidos(0);
        usuario.setCuentaBloqueadaHasta(null);
        usuarioRepository.save(usuario);

        tokenRecuperacion.setUsado(true);
        tokenRecuperacionRepository.save(tokenRecuperacion);

        refreshTokenService.eliminarPorUsuario(usuario);

        return new MensajeResponse("Contraseña restablecida exitosamente. Ya puedes iniciar sesión con tu nueva contraseña.");
    }

    public AuthResponse refreshToken(String refreshTokenStr) {
        RefreshToken refreshToken = refreshTokenService.verificarRefreshToken(refreshTokenStr);
        Usuario usuario = refreshToken.getUsuario();

        String newAccessToken = jwtTokenProvider.generateTokenFromEmail(usuario.getEmail());

        return new AuthResponse(newAccessToken, refreshToken.getToken(), usuario.getId(),
                usuario.getNombre(), usuario.getApellido(), usuario.getApodo(),
                usuario.getEmail(), usuario.getEmailVerificado());
    }

    public MensajeResponse logout(String refreshTokenStr) {
        try {
            RefreshToken refreshToken = refreshTokenService.verificarRefreshToken(refreshTokenStr);
            refreshTokenService.eliminarPorUsuario(refreshToken.getUsuario());
        } catch (Exception e) {
            log.debug("Token de refresh ya no existía al intentar logout");
        }
        return new MensajeResponse("Sesión cerrada exitosamente.");
    }

    private void verificarBloqueo(Usuario usuario) {
        if (usuario.getCuentaBloqueadaHasta() != null
                && usuario.getCuentaBloqueadaHasta().isAfter(LocalDateTime.now())) {
            long minutosRestantes = java.time.Duration.between(
                    LocalDateTime.now(), usuario.getCuentaBloqueadaHasta()).toMinutes() + 1;
            throw new CuentaBloqueadaException(
                    "Tu cuenta está temporalmente bloqueada. Intenta de nuevo en " + minutosRestantes + " minutos.");
        }

        if (usuario.getCuentaBloqueadaHasta() != null
                && usuario.getCuentaBloqueadaHasta().isBefore(LocalDateTime.now())) {
            usuario.setIntentosFallidos(0);
            usuario.setCuentaBloqueadaHasta(null);
            usuarioRepository.save(usuario);
        }
    }

    private void manejarLoginFallido(Usuario usuario) {
        int intentos = usuario.getIntentosFallidos() + 1;
        usuario.setIntentosFallidos(intentos);

        if (intentos >= maxLoginAttempts) {
            usuario.setCuentaBloqueadaHasta(LocalDateTime.now().plusMinutes(lockoutDurationMinutes));
            usuarioRepository.save(usuario);
            throw new CuentaBloqueadaException(
                    "Has superado el número máximo de intentos. Tu cuenta ha sido bloqueada temporalmente por " + lockoutDurationMinutes + " minutos.");
        }

        usuarioRepository.save(usuario);
    }

    private String buildLoginFailedMessage(Usuario usuario) {
        int intentosRestantes = maxLoginAttempts - usuario.getIntentosFallidos();
        if (intentosRestantes <= 2) {
            return "Contraseña incorrecta. Te quedan " + intentosRestantes + " intentos antes de que tu cuenta sea bloqueada temporalmente.";
        }
        return "El correo electrónico o la contraseña son incorrectos. Verifica tus datos e intenta de nuevo.";
    }

    private void validarFortalezaPassword(String password) {
        if (!PASSWORD_PATTERN.matcher(password).matches()) {
            throw new RuntimeException(
                    "La contraseña debe tener entre 8 y 128 caracteres, incluyendo una mayúscula, una minúscula, un número y al menos un símbolo (por ejemplo . @ # !). No uses espacios.");
        }
    }

}
