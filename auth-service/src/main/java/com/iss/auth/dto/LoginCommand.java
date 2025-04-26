package com.iss.auth.dto;

import lombok.Data;

@Data
public class LoginCommand {
    private String username;
    private String password;
    private String email;
}
