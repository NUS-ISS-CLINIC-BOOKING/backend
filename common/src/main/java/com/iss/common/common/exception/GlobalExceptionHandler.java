package com.iss.common.common.exception;

import com.iss.common.common.response.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 处理参数错误、校验失败等
    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorResponse handleIllegalArgumentException(IllegalArgumentException ex) {
        return new ErrorResponse(400, ex.getMessage());
    }

    // 处理通用未知异常
    @ExceptionHandler(Exception.class)
    public ErrorResponse handleException(Exception ex) {
        ex.printStackTrace(); // 也可以打正式日志
        return new ErrorResponse(500, "Internal Server Error");
    }
}
