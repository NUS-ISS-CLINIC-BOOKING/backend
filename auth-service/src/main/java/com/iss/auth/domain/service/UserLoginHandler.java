package com.iss.auth.domain.service;

import com.iss.auth.domain.entity.User;

public interface UserLoginHandler {
    void handle(User user);
}
