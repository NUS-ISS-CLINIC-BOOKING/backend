package com.iss.gateway.filter;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;

@Component
public class JwtSessionAuthenticationFilter implements WebFilter {

    private final JwtDecoder jwtDecoder;

    // 构造器注入 JwtDecoder
    public JwtSessionAuthenticationFilter(JwtDecoder jwtDecoder) {
        this.jwtDecoder = jwtDecoder;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        // 从 Cookie 中获取 Session ID
        String sessionId = request.getCookies().getFirst("SESSIONID") != null
                ? request.getCookies().getFirst("SESSIONID").getValue()
                : null;

        if (sessionId == null) {
            return Mono.error(new RuntimeException("Session ID is missing"));
        }

        // 假设你的应用将 JWT 存储在 Session 中
        String jwtToken = getJwtFromSession(sessionId);

        // 如果 session 中没有找到 JWT，则直接返回
        if (jwtToken == null) {
            return Mono.error(new RuntimeException("JWT is missing from session"));
        }

        // 校验 JWT Token
        try {
            Jwt jwt = jwtDecoder.decode(jwtToken);  // 解码 JWT
            exchange.getAttributes().put("user", jwt.getClaims()); // 将用户信息放入 exchange
        } catch (Exception e) {
            return Mono.error(new RuntimeException("Invalid or expired JWT"));
        }

        return chain.filter(exchange); // 如果验证成功，继续处理请求
    }

    // 假设我们从 Session 中获取 JWT 的方法
    private String getJwtFromSession(String sessionId) {
        // 这里可以根据 sessionId 从 Redis 或数据库中获取 JWT
        // 示例：从 Redis 获取
        // return redisTemplate.opsForValue().get("session:" + sessionId);
        return "mockJwtToken"; // 假数据
    }
}
