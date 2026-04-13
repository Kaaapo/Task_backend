package com.taskmanager.security;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimitFilter extends OncePerRequestFilter {

    private final ConcurrentHashMap<String, Bucket> authBuckets = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Bucket> resetBuckets = new ConcurrentHashMap<>();

    @Value("${rate-limit.auth.requests-per-minute:10}")
    private int authRequestsPerMinute;

    @Value("${rate-limit.reset.requests-per-hour:3}")
    private int resetRequestsPerHour;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();

        if (!path.startsWith("/api/auth/")) {
            filterChain.doFilter(request, response);
            return;
        }

        String clientIp = obtenerIpCliente(request);

        if (path.contains("/solicitar-reset") || path.contains("/reset-password")) {
            Bucket bucket = resetBuckets.computeIfAbsent(clientIp, k -> crearResetBucket());
            if (!bucket.tryConsume(1)) {
                enviarRateLimitResponse(response);
                return;
            }
        }

        Bucket bucket = authBuckets.computeIfAbsent(clientIp, k -> crearAuthBucket());
        if (!bucket.tryConsume(1)) {
            enviarRateLimitResponse(response);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private Bucket crearAuthBucket() {
        return Bucket.builder()
                .addLimit(Bandwidth.simple(authRequestsPerMinute, Duration.ofMinutes(1)))
                .build();
    }

    private Bucket crearResetBucket() {
        return Bucket.builder()
                .addLimit(Bandwidth.simple(resetRequestsPerHour, Duration.ofHours(1)))
                .build();
    }

    private String obtenerIpCliente(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty()) {
            return xRealIp;
        }
        return request.getRemoteAddr();
    }

    private void enviarRateLimitResponse(HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(
                "{\"status\":429,\"error\":\"Too Many Requests\",\"message\":\"Demasiadas solicitudes. Intenta de nuevo más tarde.\",\"code\":\"RATE_LIMIT_EXCEEDED\"}"
        );
    }
}
