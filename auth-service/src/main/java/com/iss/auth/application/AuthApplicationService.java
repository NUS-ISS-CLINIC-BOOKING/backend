package com.iss.auth.application;
import com.iss.auth.domain.entity.User;
import com.iss.auth.domain.service.*;
import com.iss.auth.domain.vo.GenderType;
import com.iss.auth.domain.vo.UserType;
import com.iss.auth.domain.repository.UserRepository;
import com.iss.auth.dto.LoginCommand;
import com.iss.auth.dto.LoginResult;
import com.iss.auth.dto.RegisterResult;
import com.iss.auth.infrastructure.jwt.JwtTokenProvider;
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
    private final JwtTokenProvider jwtTokenProvider;

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

        UserLoginHandler handler = handlerMap.get(user.getUserType());
        if (handler == null) {
            throw new IllegalArgumentException("Unsupported user type: " + user.getUserType());
        }
        handler.handle(user);

        String token = jwtTokenProvider.generateToken(user.getId(), user.getUserType().name());
        return new LoginResult(user.getId(), token);
    }

    public RegisterResult register(RegisterCommand cmd) {

        User existing = userRepository.findByEmail(cmd.getEmail());
        if (existing != null) {
            throw new IllegalArgumentException("Email already registered");
        }

        String encodedPassword = passwordEncoder.encode(cmd.getPassword());

        User user = new User(
                null,
                cmd.getName(),
                GenderType.fromOrdinal(cmd.getGender()),
                cmd.getEmail(),
                encodedPassword,
                UserType.fromOrdinal(cmd.getUserType())
        );

        // 4. 保存到数据库
        userRepository.save(user);

        // 5. 返回注册结果
        return new RegisterResult(user.getId(), "Register success");
    }
}
