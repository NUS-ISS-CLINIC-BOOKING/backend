package com.iss.auth.application;

import com.iss.auth.domain.entity.User;
import com.iss.auth.domain.repository.UserRepository;
import com.iss.auth.domain.service.AdminLoginHandlerImpl;
import com.iss.auth.domain.service.ClinicStaffLoginHandlerImpl;
import com.iss.auth.domain.service.DoctorLoginHandlerImpl;
import com.iss.auth.domain.service.PatientLoginHandlerImpl;
import com.iss.auth.domain.vo.GenderType;
import com.iss.auth.domain.vo.UserType;
import com.iss.auth.dto.LoginCommand;
import com.iss.auth.dto.LoginResult;
import com.iss.auth.dto.RegisterCommand;
import com.iss.auth.dto.RegisterResult;
import com.iss.auth.infrastructure.email.EmailService;
import com.iss.common.jwt.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthApplicationServiceTest {

    @Mock
    private PatientLoginHandlerImpl mockPatientLoginHandler;
    @Mock
    private DoctorLoginHandlerImpl mockDoctorLoginHandler;
    @Mock
    private AdminLoginHandlerImpl mockAdminLoginHandler;
    @Mock
    private ClinicStaffLoginHandlerImpl mockClinicStaffLoginHandler;
    @Mock
    private UserRepository mockUserRepository;
    @Mock
    private PasswordEncoder mockPasswordEncoder;
    @Mock
    private JwtTokenProvider mockJwtTokenProvider;
    @Mock
    private EmailService mockEmailService;
    @Mock
    private StringRedisTemplate mockRedisTemplate;

    private AuthApplicationService authApplicationServiceUnderTest;

    @BeforeEach
    void setUp() {
        authApplicationServiceUnderTest = new AuthApplicationService(mockPatientLoginHandler, mockDoctorLoginHandler,
                mockAdminLoginHandler, mockClinicStaffLoginHandler, mockUserRepository, mockPasswordEncoder,
                mockJwtTokenProvider);
        ReflectionTestUtils.setField(authApplicationServiceUnderTest, "emailService", mockEmailService);
        ReflectionTestUtils.setField(authApplicationServiceUnderTest, "redisTemplate", mockRedisTemplate);
    }

    @Test
    void testInit() {
        // Setup
        // Run the test
        authApplicationServiceUnderTest.init();

        // Verify the results
    }

    @Test
    void testLogin() {
        // Setup
        final LoginCommand request = new LoginCommand();
        request.setEmail("email");
        request.setPassword("password");

        final LoginResult expectedResult = new LoginResult(0L, "token");

        // Configure UserRepository.findByEmail(...).
        final User user = new User(0L, "name", GenderType.Male, "email", "password", UserType.PATIENT, 0, "speciality");
        when(mockUserRepository.findByEmail("email")).thenReturn(user);

        when(mockJwtTokenProvider.generateToken(0L, "name")).thenReturn("token");

        // Run the test
        final LoginResult result = authApplicationServiceUnderTest.login(request);

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testLogin_UserRepositoryReturnsNull() {
        // Setup
        final LoginCommand request = new LoginCommand();
        request.setEmail("email");
        request.setPassword("password");

        when(mockUserRepository.findByEmail("email")).thenReturn(null);

        // Run the test
        assertThatThrownBy(() -> authApplicationServiceUnderTest.login(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void testRegister_ThrowsIllegalArgumentException() {
        // Setup
        final RegisterCommand cmd = new RegisterCommand();
        cmd.setEmail("email");
        cmd.setPassword("password");
        cmd.setName("name");
        cmd.setGender(0);
        cmd.setUserType(0);
        cmd.setClinicID(0);
        cmd.setSpeciality("speciality");

        // Configure UserRepository.findByEmail(...).
        final User user = new User(0L, "name", GenderType.Male, "email", "password", UserType.PATIENT, 0, "speciality");
        when(mockUserRepository.findByEmail("email")).thenReturn(user);

        // Run the test
        assertThatThrownBy(() -> authApplicationServiceUnderTest.register(cmd))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void testRegister_UserRepositoryFindByEmailReturnsNull() {
        // Setup
        final RegisterCommand cmd = new RegisterCommand();
        cmd.setEmail("email");
        cmd.setPassword("password");
        cmd.setName("name");
        cmd.setGender(0);
        cmd.setUserType(0);
        cmd.setClinicID(0);
        cmd.setSpeciality("speciality");

        final RegisterResult expectedResult = new RegisterResult(0L, "Register success");
        when(mockUserRepository.findByEmail("email")).thenReturn(null);
        when(mockPasswordEncoder.encode("password")).thenReturn("password");

        // Run the test
        final RegisterResult result = authApplicationServiceUnderTest.register(cmd);

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
        verify(mockUserRepository).save(any(User.class));
    }
}
