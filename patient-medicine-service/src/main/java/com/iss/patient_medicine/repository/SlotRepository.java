package com.iss.patient_medicine.repository;

import com.iss.patient_medicine.util.DateTimeConverter;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import javax.sql.DataSource;
import java.sql.*;
import java.util.UUID;

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

        String sql = "SELECT patient_id FROM schedule WHERE start_time = ? AND doctor_id = ?";  // 添加 date 条件
        try (Connection conn = dataSource.getConnection();
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

    public boolean markSlotAsPaid(String date, String startTime, Long patientId, int clinicId, Long doctorId) {
        try {
            String datetimeStr = date + " " + startTime + ":00";  // 格式化时间为 "yyyy-MM-dd HH:mm:ss"

            String updateSql = "UPDATE schedule SET paid = ? " +
                    "WHERE patient_id = ? AND start_time = ? AND doctor_id = ? AND clinic_id = ?";

            try (Connection conn = dataSource.getConnection();
                 PreparedStatement ps = conn.prepareStatement(updateSql)) {

                ps.setBoolean(1, true);
                ps.setLong(2, patientId);
                ps.setString(3, datetimeStr);
                ps.setLong(4, doctorId);
                ps.setInt(5, clinicId);

                int rowsUpdated = ps.executeUpdate();
                return rowsUpdated > 0;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Database error while updating slot to paid: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new IllegalArgumentException(
                    "Invalid time slot format: date=" + date + ", time=" + startTime, e
            );
        }
    }


    public boolean bookSlot(String date, String startime, Long patientId, int clinicId, Long doctorId) {
        try {
            String datetimeStr = date + " " + startime + ":00";  // 假设 startime 是 "HH:MM"

            String insertSql = "INSERT INTO schedule (id, patient_id, start_time, doctor_id, clinic_id, duration) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";

            try (Connection conn = dataSource.getConnection();
                 PreparedStatement ps = conn.prepareStatement(insertSql)) {

                UUID uuid = UUID.randomUUID();
                long id = uuid.getMostSignificantBits();

                ps.setLong(1, id);
                ps.setLong(2, patientId);
                ps.setString(3, datetimeStr);
                ps.setLong(4, doctorId);
                ps.setInt(5, clinicId);
                ps.setInt(6, 30);        // duration

                int rowsAffected = ps.executeUpdate();
                return rowsAffected > 0;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Database error: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new IllegalArgumentException(
                    "Invalid time slot format: date=" + date + ", time=" + startime, e
            );
        }
    }


}