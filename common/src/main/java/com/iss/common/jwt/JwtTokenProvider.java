package com.iss.common.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private SecretKey SECRET_KEY;
    private static final long EXPIRATION_TIME = 86400000; // 1天（单位：毫秒）


    @PostConstruct
    public void init() {
        byte[] keyBytes = "MySuperSecretKey1234567890MySuperSecretKey1234567890".getBytes(StandardCharsets.UTF_8);
        this.SECRET_KEY = Keys.hmacShaKeyFor(keyBytes);
    }

    // 生成 token
    public String generateToken(Long userId, String role) {
        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    // 解析 token 获取 userId
    public Long getUserIdFromToken(String token) {
        Claims claims = parseClaims(token);
        return Long.parseLong(claims.getSubject());
    }

    // 解析 token 获取 role
    public String getUserRole(String token) {
        Claims claims = parseClaims(token);
        return claims.get("role", String.class);
    }

    // 校验 token 是否有效
    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (ExpiredJwtException e) {
            System.out.println("Token expired: " + e.getMessage());
        } catch (UnsupportedJwtException e) {
            System.out.println("Unsupported token: " + e.getMessage());
        } catch (MalformedJwtException e) {
            System.out.println("Malformed token: " + e.getMessage());
        } catch (SignatureException e) {
            System.out.println("Invalid signature: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Empty token: " + e.getMessage());
        }
        return false;
    }


    // 内部封装解析逻辑
    private Claims parseClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }
}
