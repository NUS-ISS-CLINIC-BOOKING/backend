package com.iss.auth.domain.repository;

import com.iss.auth.domain.model.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findByUsername(String username);
}
