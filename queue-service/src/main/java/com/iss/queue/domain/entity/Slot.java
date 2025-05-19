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
    private boolean available;

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