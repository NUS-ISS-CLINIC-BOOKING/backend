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
import java.util.ArrayList;
import java.util.List;
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
}
