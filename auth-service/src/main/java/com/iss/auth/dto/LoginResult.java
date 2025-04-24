package com.iss.auth.dto;

public class LoginResult {
    private Long userId;
    private String token;

    public LoginResult(Long userId, String token) {
        this.userId = userId;
        this.token = token;
    }

    // Getters and Setters
}
