package com.iss.queue.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Slot {
    private String date;
    private String startTime;
    private Long patientId;
    private Long doctorId;
    private int clinicId;
    private boolean available;

    public Slot(String date, String time, Long patientId, boolean b) {
        this.date = date;
        this.startTime = time;
        this.patientId = patientId;
        this.available = b;
    }

    public boolean bookSlot(Long patientId) {
        if (available == false) {
            return false;
        } else {
            this.patientId = patientId;
            available = false;
            return true;
        }
    }

    public boolean isAvailable() {
        return available;
    }
}