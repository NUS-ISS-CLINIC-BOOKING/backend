package com.iss.auth.domain.service;

import com.iss.auth.domain.model.User;

public interface AuthService {
    boolean verifyPassword(User user, String rawPassword);
    String generateToken(User user);
}
