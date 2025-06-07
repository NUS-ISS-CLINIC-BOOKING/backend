package com.iss.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/actuator/prometheus").permitAll() // ✅ 放行 Prometheus
                        .anyRequest().authenticated()                        // 其他接口继续鉴权
                )
                .csrf(csrf -> csrf.disable());                           // ✅ 关闭 CSRF，避免 POST 被拒
        return http.build();
    }
}
