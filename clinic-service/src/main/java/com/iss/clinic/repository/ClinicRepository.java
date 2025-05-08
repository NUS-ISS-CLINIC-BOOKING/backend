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

@Repository
public class ClinicRepository {
    @Autowired
    private DataSource dataSource; // 直接注入数据源

    public List<Clinic> findAll() {
        String sql = "SELECT id, name, address, phone, longitude, latitude, staff_list_id FROM Clinic";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            List<Clinic> clinics = new ArrayList<>();
            while (rs.next()) {
                clinics.add(mapRowToClinic(rs));
            }
            return clinics;

        } catch (SQLException e) {
            throw new RuntimeException("Database query failed", e);
        }
    }

    private Clinic mapRowToClinic(ResultSet rs) throws SQLException {
        return new Clinic(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("address"),
                rs.getString("phone"),
                rs.getDouble("longitude"),
                rs.getDouble("latitude"),
                rs.getInt("staff_list_id")
        );
    }
}