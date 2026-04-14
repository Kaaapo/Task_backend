package com.taskmanager.exception;

import java.util.Map;

public class ResourceNotFoundException extends RuntimeException {

    private static final Map<String, String> FRIENDLY_NAMES = Map.ofEntries(
            Map.entry("Usuario", "El usuario"),
            Map.entry("Empresa", "La empresa"),
            Map.entry("Proyecto", "El proyecto"),
            Map.entry("Tarea", "La tarea"),
            Map.entry("Estado", "El estado"),
            Map.entry("Etiqueta", "La etiqueta"),
            Map.entry("TipoProyecto", "El tipo de proyecto"),
            Map.entry("ComentarioTarea", "El comentario"),
            Map.entry("MiembroEmpresa", "El miembro de la empresa"),
            Map.entry("MiembroProyecto", "El miembro del proyecto"),
            Map.entry("RefreshToken", "La sesión")
    );

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String resourceName, Long id) {
        super(buildMessage(resourceName, id));
    }

    private static String buildMessage(String resourceName, Long id) {
        String friendly = FRIENDLY_NAMES.getOrDefault(resourceName, resourceName);
        return String.format("%s solicitado no fue encontrado (ID: %d)", friendly, id);
    }
}
