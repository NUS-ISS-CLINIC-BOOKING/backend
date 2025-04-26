package com.iss.auth.controller;
import com.iss.auth.application.AuthApplicationService;
import com.iss.auth.common.response.SuccessResponse;
import com.iss.auth.dto.LoginCommand;
import com.iss.auth.dto.LoginResult;
import com.iss.auth.dto.RegisterCommand;
import com.iss.auth.dto.RegisterResult;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


/**
 * AuthController
 *
 * 负责接收用户登录请求，转发给应用服务处理。
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthApplicationService authApplicationService;

    /**
     * 用户登录接口
     *
     * @param request 登录请求参数
     * @return 登录结果，包括token等
     */
    @PostMapping("/login")
    public SuccessResponse<LoginResult> login(@RequestBody @Valid LoginCommand request) {
        return new SuccessResponse<>(authApplicationService.login(request));
    }

    @PostMapping("/register")
    public SuccessResponse<RegisterResult> register(@RequestBody @Valid RegisterCommand command) {
        return new SuccessResponse<>(authApplicationService.register(command));
    }

}
