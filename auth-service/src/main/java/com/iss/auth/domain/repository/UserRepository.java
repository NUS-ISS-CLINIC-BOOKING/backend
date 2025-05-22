package com.iss.auth.domain.repository;

import com.iss.auth.domain.entity.User;

public interface UserRepository {
    User findByEmail(String email);
    void save(User user);
    void ModifyUserHealthInfo(long userId, String healthInfo);
}
