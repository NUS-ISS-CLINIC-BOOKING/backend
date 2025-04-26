package com.iss.auth.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class) // 使用 Mockito 扩展
class UserMapperTest {

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    private UserMapper userMapper;

    @BeforeEach
    void setUp() throws SQLException {
        // 通过构造函数注入 Mock 的 Connection
        userMapper = new UserMapper(connection);

        // 统一配置 prepareStatement 返回 Mock 的 PreparedStatement
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
    }

    @Test
    void addUser_Success() throws Exception {
        // Given
        BigInteger userId = BigInteger.valueOf(12335L);
        String name = "John Doe";
        Integer sex = 1;
        String email = "john.doe@example.com";
        String password = "securePassword123";
        int roleId = 2;

        // When
        userMapper.addUser(userId, name, sex, email, password, roleId);

        // Then
        String expectedSql = "INSERT INTO User (id, name, sex, email, password, role_id) VALUES (?, ?, ?, ?, ?, ?)";
        verify(connection).prepareStatement(expectedSql);
        verify(preparedStatement).setLong(1, 12335L);
        verify(preparedStatement).setString(2, "John Doe");
        verify(preparedStatement).setInt(3, 1);
        verify(preparedStatement).setString(4, "john.doe@example.com");
        verify(preparedStatement).setString(5, "securePassword123");
        verify(preparedStatement).setInt(6, 2);
        verify(preparedStatement).executeUpdate();
    }

    @Test
    void addUser_SQLException() throws SQLException {
        // Given
        BigInteger userId = BigInteger.valueOf(12345L);
        String name = "John Doe";
        Integer sex = 1;
        String email = "john.doe@example.com";
        String password = "securePassword123";
        int roleId = 2;

        // 配置 PreparedStatement 抛出异常
        when(preparedStatement.executeUpdate()).thenThrow(new SQLException("Database error"));

        // When & Then
        assertThrows(SQLException.class, () -> {
            userMapper.addUser(userId, name, sex, email, password, roleId);
        });
    }

    @Test
    void addUser_NullName() throws SQLException {
        // Given
        BigInteger userId = BigInteger.valueOf(12345L);
        String name = null; // 测试 name 为 null
        Integer sex = 1;
        String email = "john.doe@example.com";
        String password = "securePassword123";
        int roleId = 2;

        // When
        userMapper.addUser(userId, name, sex, email, password, roleId);

        // Then
        verify(preparedStatement).setString(2, null); // 验证参数设置为 null
    }

    @Test
    void addUser_EmptyPassword() throws SQLException {
        // Given
        BigInteger userId = BigInteger.valueOf(12345L);
        String name = "John Doe";
        Integer sex = 1;
        String email = "john.doe@example.com";
        String password = ""; // 测试空密码
        int roleId = 2;

        // When
        userMapper.addUser(userId, name, sex, email, password, roleId);

        // Then
        verify(preparedStatement).setString(5, ""); // 验证密码参数设置为空字符串
    }
}