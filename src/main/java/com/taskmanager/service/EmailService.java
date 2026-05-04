package com.taskmanager.service;

import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClient;

@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    private final RestClient sendGridClient;
    private final ObjectMapper objectMapper;

    @Value("${sendgrid.api-key:}")
    private String apiKey;

    @Value("${sendgrid.from-email:}")
    private String fromEmail;

    @Value("${sendgrid.from-name:}")
    private String fromName;

    @Value("${app.frontend-url}")
    private String frontendUrl;

    @Value("${app.name}")
    private String appName;

    public EmailService(@Qualifier("sendGridRestClient") RestClient sendGridRestClient, ObjectMapper objectMapper) {
        this.sendGridClient = sendGridRestClient;
        this.objectMapper = objectMapper;
    }

    public void enviarVerificacionEmail(String destinatario, String nombre, String token) {
        String verificationUrl = frontendUrl + "/verificar-email?token=" + token;
        String subject = "Verifica tu cuenta en " + appName;
        String html = buildVerificacionHtml(nombre, verificationUrl);
        enviarPorSendGrid(destinatario, subject, html);
    }

    public void enviarRecuperacionPassword(String destinatario, String nombre, String token) {
        String resetUrl = frontendUrl + "/reset-password?token=" + token;
        String subject = "Recupera tu contraseña en " + appName;
        String html = buildRecuperacionHtml(nombre, resetUrl);
        enviarPorSendGrid(destinatario, subject, html);
    }

    private void enviarPorSendGrid(String destinatario, String subject, String html) {
        if (!StringUtils.hasText(apiKey)) {
            log.error("SendGrid: falta sendgrid.api-key (variable SENDGRID_API_KEY)");
            throw new RuntimeException("No se pudo enviar el correo: falta la API key de SendGrid.");
        }
        if (!StringUtils.hasText(fromEmail)) {
            log.error("SendGrid: falta sendgrid.from-email (variable SENDGRID_FROM_EMAIL)");
            throw new RuntimeException("No se pudo enviar el correo: falta el remitente verificado en SendGrid.");
        }

        String displayName = StringUtils.hasText(fromName) ? fromName : appName;
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("personalizations", List.of(
                Map.of("to", List.of(Map.of("email", destinatario)))));
        payload.put("from", Map.of("email", fromEmail.trim(), "name", displayName));
        payload.put("subject", subject);
        payload.put("content", List.of(Map.of("type", "text/html", "value", html)));

        String json;
        try {
            json = objectMapper.writeValueAsString(payload);
        } catch (JsonProcessingException e) {
            log.error("SendGrid: error serializando JSON: {}", e.getMessage());
            throw new RuntimeException("No se pudo preparar el correo electrónico.");
        }

        try {
            sendGridClient.post()
                    .uri("/v3/mail/send")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey.trim())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(json)
                    .retrieve()
                    .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(), (request, response) -> {
                        byte[] errBody = response.getBody().readAllBytes();
                        String err = new String(errBody, StandardCharsets.UTF_8);
                        log.error("SendGrid rechazó el envío a {}: {} {}", destinatario, response.getStatusCode(), err);
                        throw new RuntimeException("SendGrid no pudo enviar el correo (" + response.getStatusCode() + ").");
                    })
                    .toBodilessEntity();
            log.info("Correo enviado correctamente a {} (SendGrid)", destinatario);
        } catch (RuntimeException e) {
            if (e.getMessage() != null && e.getMessage().startsWith("SendGrid")) {
                throw e;
            }
            log.error("Fallo de red o cliente al enviar a {}: {}", destinatario, e.getMessage(), e);
            throw new RuntimeException("No se pudo enviar el correo electrónico. Intenta de nuevo en unos minutos.");
        }
    }

    private String buildVerificacionHtml(String nombre, String url) {
        return """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
            </head>
            <body style="margin:0;padding:0;background-color:#f4f4f5;font-family:-apple-system,BlinkMacSystemFont,'Segoe UI',Roboto,sans-serif;">
                <table width="100%%" cellpadding="0" cellspacing="0" style="background-color:#f4f4f5;padding:40px 0;">
                    <tr>
                        <td align="center">
                            <table width="600" cellpadding="0" cellspacing="0" style="background-color:#ffffff;border-radius:12px;overflow:hidden;box-shadow:0 4px 6px rgba(0,0,0,0.07);">
                                <tr>
                                    <td style="background:linear-gradient(135deg,#3b82f6,#8b5cf6);padding:32px 40px;text-align:center;">
                                        <h1 style="color:#ffffff;margin:0;font-size:28px;font-weight:700;">%s</h1>
                                    </td>
                                </tr>
                                <tr>
                                    <td style="padding:40px;">
                                        <h2 style="color:#18181b;margin:0 0 16px;font-size:22px;">¡Hola, %s!</h2>
                                        <p style="color:#52525b;font-size:16px;line-height:1.6;margin:0 0 24px;">
                                            Gracias por registrarte. Para completar tu registro y activar tu cuenta,
                                            por favor verifica tu dirección de correo electrónico haciendo clic en el botón de abajo.
                                        </p>
                                        <table width="100%%" cellpadding="0" cellspacing="0">
                                            <tr>
                                                <td align="center" style="padding:8px 0 32px;">
                                                    <a href="%s" style="background:linear-gradient(135deg,#3b82f6,#8b5cf6);color:#ffffff;padding:14px 40px;border-radius:8px;text-decoration:none;font-size:16px;font-weight:600;display:inline-block;">
                                                        Verificar mi cuenta
                                                    </a>
                                                </td>
                                            </tr>
                                        </table>
                                        <p style="color:#71717a;font-size:14px;line-height:1.5;margin:0 0 8px;">
                                            Si no puedes hacer clic en el botón, copia y pega este enlace en tu navegador:
                                        </p>
                                        <p style="color:#3b82f6;font-size:13px;word-break:break-all;margin:0 0 24px;">%s</p>
                                        <hr style="border:none;border-top:1px solid #e4e4e7;margin:24px 0;">
                                        <p style="color:#a1a1aa;font-size:13px;margin:0;">
                                            Este enlace expira en 24 horas. Si no creaste esta cuenta, puedes ignorar este correo.
                                        </p>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </body>
            </html>
            """.formatted(appName, nombre, url, url);
    }

    private String buildRecuperacionHtml(String nombre, String url) {
        return """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
            </head>
            <body style="margin:0;padding:0;background-color:#f4f4f5;font-family:-apple-system,BlinkMacSystemFont,'Segoe UI',Roboto,sans-serif;">
                <table width="100%%" cellpadding="0" cellspacing="0" style="background-color:#f4f4f5;padding:40px 0;">
                    <tr>
                        <td align="center">
                            <table width="600" cellpadding="0" cellspacing="0" style="background-color:#ffffff;border-radius:12px;overflow:hidden;box-shadow:0 4px 6px rgba(0,0,0,0.07);">
                                <tr>
                                    <td style="background:linear-gradient(135deg,#ef4444,#f97316);padding:32px 40px;text-align:center;">
                                        <h1 style="color:#ffffff;margin:0;font-size:28px;font-weight:700;">%s</h1>
                                    </td>
                                </tr>
                                <tr>
                                    <td style="padding:40px;">
                                        <h2 style="color:#18181b;margin:0 0 16px;font-size:22px;">Hola, %s</h2>
                                        <p style="color:#52525b;font-size:16px;line-height:1.6;margin:0 0 24px;">
                                            Recibimos una solicitud para restablecer la contraseña de tu cuenta.
                                            Haz clic en el botón de abajo para crear una nueva contraseña.
                                        </p>
                                        <table width="100%%" cellpadding="0" cellspacing="0">
                                            <tr>
                                                <td align="center" style="padding:8px 0 32px;">
                                                    <a href="%s" style="background:linear-gradient(135deg,#ef4444,#f97316);color:#ffffff;padding:14px 40px;border-radius:8px;text-decoration:none;font-size:16px;font-weight:600;display:inline-block;">
                                                        Restablecer contraseña
                                                    </a>
                                                </td>
                                            </tr>
                                        </table>
                                        <p style="color:#71717a;font-size:14px;line-height:1.5;margin:0 0 8px;">
                                            Si no puedes hacer clic en el botón, copia y pega este enlace:
                                        </p>
                                        <p style="color:#3b82f6;font-size:13px;word-break:break-all;margin:0 0 24px;">%s</p>
                                        <hr style="border:none;border-top:1px solid #e4e4e7;margin:24px 0;">
                                        <p style="color:#a1a1aa;font-size:13px;margin:0;">
                                            Este enlace expira en 1 hora. Si no solicitaste restablecer tu contraseña, ignora este correo.
                                            Tu contraseña actual permanecerá sin cambios.
                                        </p>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </body>
            </html>
            """.formatted(appName, nombre, url, url);
    }
}
