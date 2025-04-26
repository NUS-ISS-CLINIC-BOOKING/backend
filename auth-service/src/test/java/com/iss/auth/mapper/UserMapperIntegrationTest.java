package com.iss.auth.mapper;

import org.junit.jupiter.api.*;
import java.math.BigInteger;
import java.sql.*;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserMapperIntegrationTest {

    private static UserMapper userMapper;
    private static BigInteger testUserId;

    // 提取数据库配置（与 UserMapper 一致）
    private static final String URL = "jdbc:mysql://101.42.116.10:3306/clinic_booking";
    private static final String USER = "root";
    private static final String PASSWORD = "Root123456@";

    @BeforeAll
    static void setUp() throws Exception {
        userMapper = new UserMapper();
        testUserId = BigInteger.valueOf(System.currentTimeMillis());
    }

    @Test
    @Order(1)
    void addUser_ShouldConnectAndInsertSuccessfully() throws SQLException {
        // 测试数据
        String name = "集成测试用户";
        Integer sex = 0;
        String email = "integration-test@example.com";
        String password = "test-password";
        int roleId = 999; // 使用测试专用 role_id

        // 执行插入
        assertDoesNotThrow(() -> {
            userMapper.addUser(testUserId, name, sex, email, password, roleId);
        }, "数据库连接或写入失败，请检查网络、权限及配置");

        // 验证数据是否存在
        boolean exists = checkUserExists(testUserId);
        assertTrue(exists, "数据未成功写入数据库");
    }

    @Test
    @Order(2)
    void cleanupTestData() throws SQLException {
        // 直接使用新连接清理数据（不再依赖 UserMapper 的连接）
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement("DELETE FROM User WHERE id = ?")) {
            ps.setLong(1, testUserId.longValue());
            ps.executeUpdate();
        }
        assertFalse(checkUserExists(testUserId), "测试数据清理失败");
    }

    // 辅助方法：使用独立连接检查数据
    private boolean checkUserExists(BigInteger userId) throws SQLException {
        String sql = "SELECT 1 FROM User WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId.longValue());
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }
}