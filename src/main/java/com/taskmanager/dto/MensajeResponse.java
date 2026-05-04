package com.taskmanager.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MensajeResponse {
    private String mensaje;

    /**
     * Indica si el envío por SMTP tuvo éxito (registro, reenvío de verificación, recuperación de contraseña).
     * {@code null} cuando el mensaje no implica envío de correo.
     */
    private Boolean correoEnviado;

    public MensajeResponse(String mensaje) {
        this.mensaje = mensaje;
        this.correoEnviado = null;
    }

    public MensajeResponse(String mensaje, Boolean correoEnviado) {
        this.mensaje = mensaje;
        this.correoEnviado = correoEnviado;
    }
}
