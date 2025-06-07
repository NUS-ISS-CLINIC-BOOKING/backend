package com.iss.gateway.filter;

import com.iss.common.jwt.JwtProperties;
import com.iss.common.jwt.JwtTokenProvider;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter implements GlobalFilter, Ordered {

    private final JwtTokenProvider jwtTokenProvider;
    private final JwtProperties jwtProperties;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();

        if (isWhitelisted(path)) {
            return chain.filter(exchange);
        }

        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return unauthorized(exchange, "Missing or invalid Authorization header");
        }

        String token = authHeader.substring(7);
        try {
            if (!jwtTokenProvider.validateToken(token)) {
                return unauthorized(exchange, "Invalid JWT token");
            }

            Long userId = jwtTokenProvider.getUserIdFromToken(token);
            String role = jwtTokenProvider.getUserRole(token);

            ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                    .header("X-User-Id", String.valueOf(userId))
                    .header("X-User-Role", role)
                    .build();

            return chain.filter(exchange.mutate().request(mutatedRequest).build());

        } catch (JwtException | IllegalArgumentException e) {
            return unauthorized(exchange, "JWT error: " + e.getMessage());
        }
    }

    private boolean isWhitelisted(String path) {
        return jwtProperties.getWhitelist().stream()
                .anyMatch(white -> path.matches(globToRegex(white)));
    }

    private String globToRegex(String glob) {
        return glob.replace("**", ".+").replace("*", "[^/]+");
    }

    private Mono<Void> unauthorized(ServerWebExchange exchange, String reason) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        exchange.getResponse().getHeaders().add(HttpHeaders.CONTENT_TYPE, "application/json");

        String responseBody = String.format("{\"code\":401,\"message\":\"%s\"}", reason);
        byte[] bytes = responseBody.getBytes(StandardCharsets.UTF_8);
        return exchange.getResponse().writeWith(Mono.just(exchange.getResponse()
                .bufferFactory()
                .wrap(bytes)));
    }


    @Override
    public int getOrder() {
        return -1;
    }
}

