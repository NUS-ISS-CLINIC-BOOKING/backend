package com.iss.auth.domain.service;

import com.iss.auth.domain.entity.User;
import org.springframework.stereotype.Service;

/**
 * 诊所工作人员登录处理器
 */
@Service
public class ClinicStaffLoginHandlerImpl implements UserLoginHandler {

    @Override
    public void handle(User user) {
        System.out.println("Clinic staff login: verify assigned clinic.");
        // 这里可以加 ClinicStaff 特有的登录逻辑，比如检查所属诊所ID是否有效
    }
}
