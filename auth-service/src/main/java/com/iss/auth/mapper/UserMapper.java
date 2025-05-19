package com.iss.auth.mapper;
import java.math.BigInteger;
import java.sql.*;

public class UserMapper {
    private final Connection connection;

    // 通过构造函数注入 Connection（测试时传入 Mock 对象）
    public UserMapper(Connection connection) {
        this.connection = connection;
    }

    // 生产环境专用构造函数（保留原有逻辑）
    public UserMapper() throws SQLException {
        final String url = "jdbc:mysql://101.42.116.10:3306/clinic_booking";
        final String dbUserName = "root";
        final String dbPassword = "Root123456@";
        this.connection = DriverManager.getConnection(url, dbUserName, dbPassword);
    }

    public void addUser(BigInteger userId, String name, Integer sex, String email, String password, int roleId) throws SQLException {
        String sql = "INSERT INTO user (id, name, sex, email, password, role_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, userId.longValue());
            statement.setString(2, name);
            statement.setInt(3, sex);
            statement.setString(4, email);
            statement.setString(5, password);
            statement.setInt(6, roleId);
            statement.executeUpdate();
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
