package com.iss.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ModifyHealthInfoCommand {
    @NotBlank(message = "Allergy information must not be blank")
    private String allergyInfo;
}
