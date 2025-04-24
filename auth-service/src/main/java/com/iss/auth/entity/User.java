package com.iss.auth.entity;

import lombok.Data;

@Data
public class User {
    private Long id;
    private String name;
    private Integer sex;
    private String email;
    private String password;
    private Integer roleId;
}
