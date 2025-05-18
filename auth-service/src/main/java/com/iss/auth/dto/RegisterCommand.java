package com.iss.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Command object for user registration request.
 */
@Data
public class RegisterCommand {

    @NotBlank(message = "Email must not be blank")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password must not be blank")
    private String password;

    @NotBlank(message = "Name must not be blank")
    private String name;

    @NotNull(message = "Gender must not be null")
    private int gender;

    @NotNull(message = "User type must not be null")
    private int userType;

    private int clinicID;

    private String speciality;
}
