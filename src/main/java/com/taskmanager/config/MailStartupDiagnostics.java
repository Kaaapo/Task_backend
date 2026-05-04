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
 * Avisa en logs si SendGrid o la URL del front en producción parecen mal configurados.
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
        String apiKey = environment.getProperty("sendgrid.api-key", "");
        String fromEmail = environment.getProperty("sendgrid.from-email", "");
        String frontend = environment.getProperty("app.frontend-url", "");

        if (!hasText(apiKey) || !hasText(fromEmail)) {
            log.warn("""
                    ========== CORREO (SendGrid) ==========
                    Falta SENDGRID_API_KEY o SENDGRID_FROM_EMAIL.
                    Crea una API Key y verifica un remitente (Single Sender) en https://app.sendgrid.com
                    y define en Railway:
                    - SENDGRID_API_KEY
                    - SENDGRID_FROM_EMAIL (debe coincidir con el remitente verificado)
                    Opcional: SENDGRID_FROM_NAME (por defecto usa app.name)
                    ========================================""");
        }

        if (frontend.contains("localhost") || !hasText(frontend)) {
            log.warn("""
                    ========== FRONTEND_URL ==========
                    app.frontend-url apunta a localhost o está vacío. Los enlaces dentro de los correos no llevarán a tu front público.
                    Define FRONTEND_URL en Railway con la URL real (ej. https://tu-app.vercel.app).
                    ===================================""");
        }

        if (log.isDebugEnabled()) {
            log.debug("Perfil activo: {}", Arrays.toString(environment.getActiveProfiles()));
        }
    }

    private static boolean hasText(String s) {
        return s != null && !s.isBlank();
    }
}
