package com.iss.clinic.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
}