package com.iss.clinic.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.iss.clinic.domain.entity.Specialty;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iss.clinic.domain.entity.Clinic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

@Repository
public class ClinicRepository {
    @Autowired
    private DataSource dataSource; // 直接注入数据源

    public List<Clinic> findAll() {
        String sql = "SELECT id, name, address, phone, longitude, latitude FROM clinic";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            List<Clinic> clinics = new ArrayList<>();
            while (rs.next()) {
                clinics.add(mapRowToClinic(rs));
            }
            return clinics;

        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private Clinic mapRowToClinic(ResultSet rs) throws SQLException {
        return new Clinic(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("address"),
                rs.getString("phone"),
                rs.getDouble("longitude"),
                rs.getDouble("latitude")
        );
    }

    public Optional<Object> findById(int clinicId) {
        String sql = "SELECT id, name, address, phone, longitude, latitude FROM clinic WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // 设置参数
            ps.setInt(1, clinicId);

            // 执行查询
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // 将结果映射到 Clinic 对象
                    return Optional.of(mapRowToClinic(rs));
                }
            }
        } catch (SQLException e) {
            // 记录错误日志（实际项目中建议使用日志框架如SLF4J）
            System.err.println("Error fetching clinic by ID: " + clinicId);
            e.printStackTrace();
        }

        return Optional.empty();
    }

    public List<Long> findStaffIdsByClinicId(int clinicId) {
        String sql = "SELECT id FROM clinic_staff_info WHERE clinic_id = ?";
        List<Long> staffIds = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            // 3. 设置参数
            statement.setInt(1, clinicId);

            // 4. 执行查询并处理结果集
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    staffIds.add(rs.getLong("id"));
                }
            }
        } catch (SQLException e) {
            // 5. 记录日志并抛出业务异常
            throw new RuntimeException(e.getMessage(), e);
        }
        return staffIds;
    }
    public List<Specialty> findSpecialtyByClinicId(int clinicId) {
        String sql = "SELECT specialty FROM clinic_staff_info WHERE clinic_id = ?";
        // 使用 Set 自动去重（需 Specialty 正确实现 equals() 和 hashCode()）
        Set<Specialty> uniqueSpecialties = new HashSet<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, clinicId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Specialty specialty = new Specialty();
                    specialty.setSpecialty(resultSet.getString("specialty"));
                    uniqueSpecialties.add(specialty); // Set 会自动去重
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error fetching specialties for clinic ID: " + clinicId, e);
        }

        return new ArrayList<>(uniqueSpecialties); // 转回 List
    }
}