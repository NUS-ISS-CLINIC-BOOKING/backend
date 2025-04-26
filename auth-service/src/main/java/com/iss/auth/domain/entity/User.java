package com.iss.auth.domain.entity;

import com.iss.auth.domain.vo.UserType;
import lombok.Getter;

@Getter
public class User {

    private Long id;
    private String name;
    private String email;
    private String password;
    private UserType userType;


    public User(Long id, String name, String email, String encryptedPassword, UserType userType) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = encryptedPassword;
        this.userType = userType;
    }
}
