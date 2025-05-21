package com.iss.auth.application;
import com.iss.auth.domain.entity.User;
import com.iss.auth.domain.service.*;
import com.iss.auth.domain.vo.GenderType;
import com.iss.auth.domain.vo.UserType;
import com.iss.auth.domain.repository.UserRepository;
import com.iss.auth.dto.LoginCommand;
import com.iss.auth.dto.LoginResult;
import com.iss.auth.dto.RegisterResult;
import com.iss.auth.infrastructure.email.EmailService;
import com.iss.auth.utils.jwt.JwtTokenProvider;
import com.iss.auth.dto.RegisterCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;

import java.time.Duration;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

import static com.iss.auth.utils.mfa.MfaUtil.generateMfaCode;

/**
 * 应用服务，负责登录流程编排
 */
@Service
@RequiredArgsConstructor
public class AuthApplicationService {

    @Autowired
    private EmailService emailService;
    @Autowired
    private StringRedisTemplate redisTemplate;
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

        // generate MFA code
        String code = generateMfaCode();
        redisTemplate.opsForValue().set("mfa_code:" + user.getId(), code, Duration.ofMinutes(5));
        emailService.send(user.getEmail(), "Your MFA Code", "Your code is: " + code);

        String token = jwtTokenProvider.generateToken(user.getId(), user.getUserType().name());
        return new LoginResult(user.getId(), token);
    }

    public RegisterResult register(RegisterCommand cmd) {
        if (UserType.fromOrdinal(cmd.getUserType()) != UserType.PATIENT && UserType.fromOrdinal(cmd.getUserType()) != UserType.ADMIN && (cmd.getClinicID() == 0 || Objects.equals(cmd.getSpeciality(), ""))) {
            // If the register type is not patient and clinic id or speciality is null, then the register cmd is invalid
            throw new IllegalArgumentException("invalid request, please specify clinic and speciality");
        }

        User existing = userRepository.findByEmail(cmd.getEmail());
        if (existing != null) {
            throw new IllegalArgumentException("User already registered");
        }
        System.out.println(cmd);
        String encodedPassword = passwordEncoder.encode(cmd.getPassword());
        System.out.println(cmd.getUserType());
        User user = new User(
                null,
                cmd.getName(),
                GenderType.fromOrdinal(cmd.getGender()),
                cmd.getEmail(),
                encodedPassword,
                UserType.fromOrdinal(cmd.getUserType()),
                cmd.getClinicID(),
                cmd.getSpeciality()
        );

        // 4. 保存到数据库
        userRepository.save(user);

        // 5. 返回注册结果
        return new RegisterResult(user.getId(), "Register success");
    }
}
