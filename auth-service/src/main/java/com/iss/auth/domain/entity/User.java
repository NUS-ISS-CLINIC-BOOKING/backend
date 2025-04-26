package com.iss.auth.domain.entity;

import com.iss.auth.domain.vo.GenderType;
import com.iss.auth.domain.vo.UserType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
public class User {

    private Long id;
    private String name;
    private GenderType gender;
    private String email;
    private String password;
    private UserType userType;


    public User(Long id, String name, GenderType gender, String email, String password, UserType userType) {
        this.id = id;
        this.gender = gender;
        this.name = name;
        this.email = email;
        this.password = password;
        this.userType = userType;
    }

    public boolean passwordMatches(String rawPassword, PasswordEncoder encoder) {
        return encoder.matches(rawPassword, this.password);
    }

    public void afterSaving(Long generatedId) {
        if (this.id != null) {
            throw new IllegalStateException("User ID is already set");
        }
        this.id = generatedId;
    }
}
