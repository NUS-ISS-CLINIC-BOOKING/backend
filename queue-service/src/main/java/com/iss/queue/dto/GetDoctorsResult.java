package com.iss.queue.dto;
import com.iss.queue.domain.entity.Doctor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetDoctorsResult {
    private List<Doctor> doctors;
    private String message;
}
