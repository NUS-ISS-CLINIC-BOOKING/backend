package com.iss.auth.controller;

import com.iss.auth.application.AuthApplicationService;
import com.iss.auth.dto.LoginCommand;
import com.iss.auth.dto.LoginResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthApplicationService authAppService;

    public AuthController(AuthApplicationService authAppService) {
        this.authAppService = authAppService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResult> login(@RequestBody LoginCommand command) {
        LoginResult result = authAppService.login(command);
        return ResponseEntity.ok(result);
    }
}
