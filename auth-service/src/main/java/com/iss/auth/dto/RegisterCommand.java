package com.iss.auth.dto;

import com.iss.auth.domain.vo.GenderType;
import com.iss.auth.domain.vo.UserType;
import lombok.Data;

@Data
public class RegisterCommand {
    private String name;
    private String email;
    private String password;
    private GenderType gender;
    private UserType userType;
}
