package com.iss.auth.dto;

import lombok.Data;

@Data
public class LoginResult {
    private Long userId;
    private String token;
    private String message;

    public LoginResult(Long userId, String token) {
        this.userId = userId;
        this.token = token;
        this.message = "login success";
    }
    // Getters and Setters
}
