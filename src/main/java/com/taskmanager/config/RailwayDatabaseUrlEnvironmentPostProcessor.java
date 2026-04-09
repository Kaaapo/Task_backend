package com.taskmanager.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * Railway (y Heroku) exponen DATABASE_URL como postgresql://user:pass@host:port/db.
 * El driver JDBC de PostgreSQL requiere jdbc:postgresql://host:port/db (sin credenciales en la URL).
 * Este procesador convierte esa URL y la inyecta con prioridad alta.
 */
@Order(Ordered.LOWEST_PRECEDENCE)
public class RailwayDatabaseUrlEnvironmentPostProcessor implements EnvironmentPostProcessor {

    private static final String SOURCE_NAME = "railwayJdbcUrl";

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        String databaseUrl = environment.getProperty("DATABASE_URL");
        if (databaseUrl == null || databaseUrl.isBlank()) {
            return;
        }
        String trimmed = databaseUrl.trim();
        if (trimmed.startsWith("jdbc:postgresql:") || trimmed.startsWith("jdbc:postgres:")) {
            return;
        }
        if (!trimmed.startsWith("postgres://") && !trimmed.startsWith("postgresql://")) {
            return;
        }
        try {
            String normalized = trimmed.replaceFirst("^postgres://", "postgresql://");
            URI uri = URI.create(normalized);
            String host = uri.getHost();
            if (host == null || host.isBlank()) {
                return;
            }
            int port = uri.getPort() > 0 ? uri.getPort() : 5432;
            String path = uri.getPath();
            String database = "postgres";
            if (path != null && path.length() > 1) {
                database = path.substring(1);
            }
            String jdbcUrl = String.format("jdbc:postgresql://%s:%d/%s", host, port, database);

            Map<String, Object> map = new HashMap<>();
            map.put("spring.datasource.url", jdbcUrl);
            environment.getPropertySources().addFirst(new MapPropertySource(SOURCE_NAME, map));
        } catch (Exception ignored) {
            // Si falla el parseo, se usa application.properties
        }
    }
}
