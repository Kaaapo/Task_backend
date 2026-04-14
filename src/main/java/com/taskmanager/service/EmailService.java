package com.taskmanager.service;

import java.io.UnsupportedEncodingException;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${app.frontend-url}")
    private String frontendUrl;

    @Value("${app.name}")
    private String appName;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void enviarVerificacionEmail(String destinatario, String nombre, String token) {
        String verificationUrl = frontendUrl + "/verificar-email?token=" + token;
        String subject = "Verifica tu cuenta en " + appName;
        String html = buildVerificacionHtml(nombre, verificationUrl);
        enviarEmail(destinatario, subject, html);
    }

    public void enviarRecuperacionPassword(String destinatario, String nombre, String token) {
        String resetUrl = frontendUrl + "/reset-password?token=" + token;
        String subject = "Recupera tu contraseña en " + appName;
        String html = buildRecuperacionHtml(nombre, resetUrl);
        enviarEmail(destinatario, subject, html);
    }

    private void enviarEmail(String destinatario, String subject, String html) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            InternetAddress from;
            try {
                from = new InternetAddress(fromEmail, appName, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                from = new InternetAddress(fromEmail);
            }
            helper.setFrom(from);
            helper.setTo(destinatario);
            helper.setSubject(subject);
            helper.setText(html, true);
            mailSender.send(message);
            log.info("Correo enviado correctamente a {}", destinatario);
        } catch (MessagingException e) {
            log.error("Error al crear el mensaje MIME para {}: {}", destinatario, e.getMessage(), e);
            throw new RuntimeException("No se pudo enviar el correo electrónico. Por favor, intenta nuevamente en unos minutos.");
        } catch (MailException e) {
            log.error("Fallo SMTP al enviar a {} ({}): {}", destinatario, e.getClass().getSimpleName(), e.getMessage(), e);
            throw new RuntimeException("No se pudo enviar el correo electrónico. Por favor, intenta nuevamente en unos minutos.");
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
