package com.iss.auth.domain.service;

import com.iss.auth.domain.entity.User;
import org.springframework.stereotype.Service;

/**
 * 医生登录处理器
 */
@Service
public class DoctorLoginHandlerImpl implements UserLoginHandler {

    @Override
    public void handle(User user) {
        System.out.println("Doctor login: verify professional license.");
        // 这里可以加医生特有的登录逻辑，比如查执业证书
    }
}
