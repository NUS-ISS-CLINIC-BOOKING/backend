package com.iss.auth.application;
import com.iss.auth.domain.entity.User;
import com.iss.auth.domain.service.*;
import com.iss.auth.domain.vo.UserType;
import com.iss.auth.domain.repository.UserRepository;
import com.iss.auth.dto.LoginCommand;
import com.iss.auth.dto.LoginResult;

import com.iss.auth.dto.RegisterCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final PasswordEncoder passwordEncoder;

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

        User user = userRepository.findByEmail(request.getEmail());
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        if (!user.passwordMatches(request.getPassword(), passwordEncoder)) {
            throw new IllegalArgumentException("Invalid password");
        }

        // 3. 分发到对应的登录处理器
        UserLoginHandler handler = handlerMap.get(user.getUserType());
        if (handler == null) {
            throw new IllegalArgumentException("Unsupported user type: " + user.getUserType());
        }
        handler.handle(user);

        return new LoginResult(123L, "1212");
    }

    public void register(RegisterCommand cmd) {
        // 1. 判断是否已注册
        User existing = userRepository.findByEmail(cmd.getEmail());
        if (existing != null) {
            throw new IllegalArgumentException("Email already registered");
        }

        // 2. 加密密码
        String encodedPassword = passwordEncoder.encode(cmd.getPassword());

        // 3. 创建 User 对象
        User user = new User(
                null,
                cmd.getName(),
                cmd.getGender(),
                cmd.getEmail(),
                encodedPassword,
                cmd.getUserType()
        );

        // 4. 保存
        userRepository.save(user);
    }

}
