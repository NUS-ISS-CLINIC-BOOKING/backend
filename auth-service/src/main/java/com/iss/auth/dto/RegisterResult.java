package com.iss.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户注册返回结果
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterResult {
    private Long userId;     // 新注册用户的ID
    private String message;  // 成功提示
}
