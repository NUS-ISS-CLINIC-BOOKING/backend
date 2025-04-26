package com.iss.auth.application;
import com.iss.auth.domain.entity.User;
import com.iss.auth.domain.service.*;
import com.iss.auth.domain.vo.UserType;
import com.iss.auth.domain.repository.UserRepository;
import com.iss.auth.dto.LoginCommand;
import com.iss.auth.dto.LoginResult;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;
import java.util.EnumMap;
import java.util.Map;

/**
 * 应用服务，负责登录流程编排
 */
@Service
@RequiredArgsConstructor
public class AuthApplicationService {

    private final PatientLoginHandlerImpl patientLoginHandler;
    private final DoctorLoginHandlerImpl doctorLoginHandler;
    private final AdminLoginHandlerImpl adminLoginHandler;
    private final ClinicStaffLoginHandlerImpl clinicStaffLoginHandler;
    private final UserRepository userRepository;

    private Map<UserType, UserLoginHandler> handlerMap;

    @PostConstruct
    public void init() {
        handlerMap = new EnumMap<>(UserType.class);
        handlerMap.put(UserType.PATIENT, patientLoginHandler);
        handlerMap.put(UserType.DOCTOR, doctorLoginHandler);
        handlerMap.put(UserType.ADMIN, adminLoginHandler);
        handlerMap.put(UserType.CLINIC_STAFF, clinicStaffLoginHandler);
    }

    public LoginResult login(LoginCommand request) {
        // 1. 根据email查找用户（假设有userRepository）
        User user = userRepository.findByEmail(request.getEmail());
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        // 2. 校验密码
//        if (!passwordEncoder.matches(request.getPassword(), user.getEncryptedPassword())) {
//            throw new IllegalArgumentException("Invalid password");
//        }

        // 3. 分发到对应的登录处理器
        UserLoginHandler handler = handlerMap.get(user.getUserType());
        if (handler == null) {
            throw new IllegalArgumentException("Unsupported user type: " + user.getUserType());
        }
        handler.handle(user);

        // 4. 生成token返回
//        String token = generateToken(user);
        return new LoginResult(123L, "1212");
    }
}
