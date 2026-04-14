package com.taskmanager.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * Avisa en logs si el correo o la URL del front en producción parecen mal configurados
 * (causa habitual de "no me llega el mail de verificación" en Railway).
 */
@Component
@Profile("!test")
@Order(2000)
public class MailStartupDiagnostics implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(MailStartupDiagnostics.class);

    private final Environment environment;

    public MailStartupDiagnostics(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void run(ApplicationArguments args) {
        String user = environment.getProperty("spring.mail.username", "");
        String pass = environment.getProperty("spring.mail.password", "");
        String frontend = environment.getProperty("app.frontend-url", "");

        if (looksLikePlaceholderMail(user, pass)) {
            log.warn("""
                    ========== CORREO SMTP ==========
                    MAIL_USERNAME / MAIL_PASSWORD no parecen configurados (o siguen siendo los valores de ejemplo).
                    Los correos de verificación y recuperación NO se enviarán hasta que configures en Railway:
                    - MAIL_USERNAME = tu cuenta Gmail completa
                    - MAIL_PASSWORD = contraseña de aplicación de 16 caracteres (Google: Cuenta → Seguridad → Verificación en 2 pasos → Contraseñas de aplicaciones)
                    Revisa también los logs al registrarte: busca "Fallo al enviar correo".
                    ==================================""");
        }

        if (frontend.contains("localhost") || frontend.isBlank()) {
            log.warn("""
                    ========== FRONTEND_URL ==========
                    app.frontend-url apunta a localhost o está vacío. Los enlaces dentro de los correos no llevarán a tu front público.
                    Define FRONTEND_URL en Railway con la URL real (ej. https://tu-app.vercel.app o http://localhost:5173 solo para pruebas).
                    ===================================""");
        }

        if (log.isDebugEnabled()) {
            log.debug("Perfil activo: {}", Arrays.toString(environment.getActiveProfiles()));
        }
    }

    private static boolean looksLikePlaceholderMail(String user, String pass) {
        if (user == null || user.isBlank() || pass == null || pass.isBlank()) {
            return true;
        }
        String u = user.toLowerCase();
        String p = pass.toLowerCase();
        return u.contains("tucorreo") || p.contains("tu_app_password") || p.equals("password");
    }
}
