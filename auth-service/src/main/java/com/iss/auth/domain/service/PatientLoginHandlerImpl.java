package com.iss.auth.domain.service;

import com.iss.auth.domain.entity.User;
import org.springframework.stereotype.Service;

/**
 * 病人登录处理器
 */
@Service
public class PatientLoginHandlerImpl implements UserLoginHandler {

    @Override
    public void handle(User user) {
        System.out.println("Patient login: basic information check.");
        // 这里可以加病人特有的登录逻辑，比如检查实名信息
    }
}
