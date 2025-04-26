package com.iss.auth.domain.service;

import com.iss.auth.domain.entity.User;
import org.springframework.stereotype.Service;

/**
 * 管理员登录处理器
 */
@Service
public class AdminLoginHandlerImpl implements UserLoginHandler {

    @Override
    public void handle(User user) {
        System.out.println("Admin login: check admin permissions.");
        // 这里可以加管理员特有的登录逻辑，比如检查后台权限
    }
}
