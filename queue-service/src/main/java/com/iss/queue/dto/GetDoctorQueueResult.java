package com.iss.queue.dto;

import com.iss.queue.domain.entity.Doctor;
import com.iss.queue.domain.entity.Slot;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetDoctorQueueResult {
    private Doctor doctor;
    private List<Slot> slots;
    private String message;
}
