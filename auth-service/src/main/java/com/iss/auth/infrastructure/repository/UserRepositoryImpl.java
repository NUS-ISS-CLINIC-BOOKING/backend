package com.iss.auth.infrastructure.repository;

import com.iss.auth.domain.entity.User;
import com.iss.auth.domain.repository.UserRepository;
import com.iss.auth.domain.vo.GenderType;
import com.iss.auth.domain.vo.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


@Repository
public class UserRepositoryImpl implements UserRepository {

    private final DataSource dataSource;

    @Autowired
    public UserRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public User findByEmail(String email) {
        String sql = "SELECT id, name, sex, email, password, role_id FROM User WHERE email = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, email);

            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                int gender = rs.getInt("sex");
                GenderType genderType = gender == 0 ? GenderType.Male : GenderType.Female;
                int roleId = rs.getInt("role_id");
                return new User(
                        rs.getLong("id"),
                        rs.getString("name"),
                        GenderType.fromOrdinal(rs.getInt("sex")),
                        rs.getString("email"),
                        rs.getString("password"),
                        UserType.fromOrdinal(rs.getInt("role_id"))
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find user by email", e);
        }
        return null;
    }

    @Override
    public void save(User user) {
        String userSql = "INSERT INTO User (name, sex, email, password, role_id) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = dataSource.getConnection()) {

            // 开启事务
            connection.setAutoCommit(false);

            try (PreparedStatement statement = connection.prepareStatement(userSql, Statement.RETURN_GENERATED_KEYS)) {

                statement.setString(1, user.getName());
                statement.setInt(2, user.getGender().toOrdinal());
                statement.setString(3, user.getEmail());
                statement.setString(4, user.getPassword());
                statement.setInt(5, user.getUserType().toOrdinal());

                statement.executeUpdate();

                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    long generatedId = generatedKeys.getLong(1);
                    user.afterSaving(generatedId);

                    //  如果不是病人，写入 clinic_staff_info 表
                    if (user.getUserType() != UserType.PATIENT) {
                        String staffSql = "INSERT INTO clinic_staff_info (user_id, clinic_id, speciality) VALUES (?, ?, ?)";

                        try (PreparedStatement staffStmt = connection.prepareStatement(staffSql)) {
                            staffStmt.setLong(1, generatedId);
                            staffStmt.setInt(2, user.getClinicID());
                            staffStmt.setString(3, user.getSpeciality());
                            staffStmt.executeUpdate();
                        }
                    }

                    // 成功后提交事务
                    connection.commit();

                } else {
                    throw new SQLException("Failed to retrieve generated user ID.");
                }
            } catch (SQLException e) {
                connection.rollback(); // 出错回滚
                throw e;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to save user", e);
        }
    }


}


