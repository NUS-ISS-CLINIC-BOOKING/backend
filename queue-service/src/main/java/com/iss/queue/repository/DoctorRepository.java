package com.iss.queue.repository;

import com.iss.queue.domain.entity.Doctor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
@Repository
public class DoctorRepository {
    @Autowired
    private DataSource dataSource;

    public List<Doctor> getDoctorsByClinicId(int clinicId) {
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
        List<Doctor> doctors = new ArrayList<>();
        for(Long staffId : staffIds) {
            String doctorName = getDoctorNameById(staffId);
            String specialty = getSpecialtyById(staffId);
            Doctor doctor = new Doctor(staffId, doctorName, specialty);
            doctors.add(doctor);
        }
        return doctors;
    }


    public String getSpecialtyById(Long doctorId) {
        String sql = "SELECT specialty FROM clinic_staff_info WHERE id =?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // 4. 设置参数
            ps.setLong(1, doctorId);

            // 5. 执行查询
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("specialty");
                }
                return null; // 未找到记录
            }
        } catch (SQLException e) {
            // 6. 记录日志并包装异常
            throw new RuntimeException(e.getMessage(), e);
        }
    }
    public String getDoctorNameById(Long doctorId) {
        String sql = "SELECT name FROM user WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // 3. 设置参数
            ps.setLong(1, doctorId);

            // 4. 执行查询
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("name");
                }
                return null; // 未找到对应医生
            }
        } catch (SQLException e) {
            // 5. 包装并抛出运行时异常
            throw new RuntimeException("Failed to get doctor name by ID: " + doctorId, e);
        }
    }

    public int getUserRoleId(Long userId) {
        String sql = "SELECT role_id FROM user WHERE id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("role_id");
                } else {
                    // 如果找不到用户，可以返回一个默认值或抛出异常
                    throw new RuntimeException("User not found with id: " + userId);
                }
            }
        } catch (SQLException e) {
            // 记录错误日志
            throw new RuntimeException("Failed to retrieve user role", e);
        }
    }

    public List<Doctor> getDoctorsBySpecialty(String specialty) {
        String sql = "SELECT id FROM clinic_staff_info WHERE specialty =?";

        // 使用批量查询避免N+1问题
        List<Long> staffIds = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, specialty);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    staffIds.add(resultSet.getLong("id"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("sql exception getDoctorsBySpecialty:" + e.getMessage(), e);
        }
        List<Doctor> doctors = new ArrayList<>();
        for(Long staffId : staffIds) {
            if(getUserRoleId(staffId) != 1) {
                continue;
            }
            String doctorName = getDoctorNameById(staffId);
            Doctor doctor = new Doctor(staffId, doctorName, specialty);
            doctors.add(doctor);
        }
        return doctors;
    }

    public List<Doctor> getDoctorsByClinicIdAndSpecialty(int clinicId, String specialty) {
        String sql = "SELECT id FROM clinic_staff_info WHERE specialty =? and clinic_id =?";

        List<Long> staffIds = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, specialty);
            statement.setInt(2, clinicId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    staffIds.add(resultSet.getLong("id"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("sql exception getDoctorsBySpecialty:" + e.getMessage(), e);
        }

        List<Doctor> doctors = new ArrayList<>();

        for(Long staffId : staffIds) {
            if(getUserRoleId(staffId) != 1) {
                continue;
            }
            String doctorName = getDoctorNameById(staffId);
            Doctor doctor = new Doctor(staffId, doctorName, specialty);
            doctors.add(doctor);
        }
        return doctors;
    }
}
