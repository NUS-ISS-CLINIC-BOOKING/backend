package com.iss.queue.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.iss.queue.util.DateTimeConverter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.*;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import com.iss.queue.domain.entity.Slot;

@Repository
public class SlotRepository {
    @Autowired
    private DataSource dataSource;

    public Long getPatientIdBySlotTime(String startTime, Long doctorId, String date) {
        Timestamp timestamp;
        try {
            timestamp = DateTimeConverter.toTimestamp(date, startTime);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid time slot format: date=" + date + ", time=" + startTime);
        }

        String sql = "SELECT patient_id FROM schedule " +
                "WHERE CONVERT_TZ(start_time, '+00:00', '+08:00') = ? " +  // 显式转换时区
                "AND doctor_id = ? ORDER BY start_time ASC ";        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setTimestamp(1, timestamp);
            ps.setLong(2, doctorId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong("patient_id"); // 返回已预约的 patient_id
                }
                return null; // 时间段可用，返回 null
            }
        } catch (SQLException e) {
            // 处理异常（建议记录日志或抛出业务异常）
            e.printStackTrace();
            return null;  // 默认返回不可用（避免因异常导致错误放行）
        }
    }

    public List<Slot> getSlotsByPatientId(Long PatienId) {
        String sql = "SELECT id, patient_id, start_time, doctor_id, clinic_id, duration FROM schedule WHERE patient_id =? ORDER BY start_time ASC";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
                 ps.setLong(1, PatienId);
                 try (ResultSet rs = ps.executeQuery()) {
                     if (rs.next()) {
                         List<Slot> slots = new ArrayList<>();
                         do {
                             long id = rs.getLong("id");
                             String startTime = rs.getString("start_time");
                             Long patientId = rs.getLong("patient_id");
                             Long doctorId = rs.getLong("doctor_id");
                             int clinicId = rs.getInt("clinic_id");
                             int duration = rs.getInt("duration");
                             String date = startTime.split(" ")[0];
                             Slot slot = new Slot(date, startTime, patientId, doctorId, clinicId, true);
                             slots.add(slot);
                         } while (rs.next());
                         return slots;
                     }
                     return null;
                 } catch (SQLException e) {
                     throw new RuntimeException(e);
                 }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public boolean bookSlot(String date, String startime, Long patientId, int clinicId, Long doctorId) {
        try {
            String datetimeStr = date + " " + startime + ":00";  // 假设 startime 是 "HH:MM"

            String insertSql = "INSERT INTO schedule (id, patient_id, start_time, doctor_id, clinic_id, duration) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";

            try (Connection conn = dataSource.getConnection();
                 PreparedStatement ps = conn.prepareStatement(insertSql)) {

                UUID uuid = UUID.randomUUID();
                long id = uuid.getMostSignificantBits();

                ps.setLong(1, id);
                ps.setLong(2, patientId);
                ps.setString(3, datetimeStr);  // 直接传字符串
                ps.setLong(4, doctorId);
                ps.setInt(5, clinicId);
                ps.setInt(6, 30);

                int rowsAffected = ps.executeUpdate();
                return rowsAffected > 0;
            } catch (SQLException e) {
                throw new RuntimeException("Database error: " + e.getMessage(), e);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException(
                    "Invalid time slot format: date=" + date + ", time=" + startime, e
            );
        }
    }
}
