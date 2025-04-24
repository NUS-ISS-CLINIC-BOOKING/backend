package com.iss.auth.application;

import com.iss.auth.domain.model.User;
import com.iss.auth.domain.repository.UserRepository;
import com.iss.auth.domain.service.AuthService;
import com.iss.auth.dto.LoginCommand;
import com.iss.auth.dto.LoginResult;
import com.iss.auth.exception.LoginFailedException;

@Service
public class AuthApplicationService {

    private final UserRepository userRepository;
    private final AuthService authService;

    public AuthApplicationService(UserRepository userRepository, AuthService authService) {
        this.userRepository = userRepository;
        this.authService = authService;
    }

    public LoginResult login(LoginCommand command) {
        User user = userRepository.findByUsername(command.getUsername())
                .orElseThrow(() -> new LoginFailedException("User not found"));

        if (!authService.verifyPassword(user, command.getPassword())) {
            throw new LoginFailedException("Invalid credentials");
        }

        String token = authService.generateToken(user);
        return new LoginResult(user.getId(), token);
    }
}
