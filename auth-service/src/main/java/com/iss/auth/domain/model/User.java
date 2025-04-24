package com.iss.auth.domain.model;

import org.springframework.security.crypto.password.PasswordEncoder;

public class User {
    private Long id;
    private String username;
    private String encryptedPassword;

    public User(Long id, String username, String encryptedPassword) {
        this.id = id;
        this.username = username;
        this.encryptedPassword = encryptedPassword;
    }

    public boolean passwordMatches(String rawPassword, PasswordEncoder encoder) {
        return encoder.matches(rawPassword, this.encryptedPassword);
    }

    // Getters
}
