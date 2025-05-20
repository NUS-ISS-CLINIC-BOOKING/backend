package com.iss.auth.controller;
import com.iss.auth.application.AuthApplicationService;
import com.iss.auth.application.UserApplicationService;
import com.iss.auth.dto.*;
import com.iss.common.common.response.SuccessResponse;
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
    private final UserApplicationService userApplicationService;

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

    @PostMapping("/health_info/{id}")
    public SuccessResponse<ModifyHealthInfoResult> modifyHealthInfo(
            @PathVariable Long id,
            @RequestBody @Valid ModifyHealthInfoCommand command) {
        ModifyHealthInfoResult result = userApplicationService.modifyHealthInfo(id, command);
        return new SuccessResponse<>(result);
    }

}
