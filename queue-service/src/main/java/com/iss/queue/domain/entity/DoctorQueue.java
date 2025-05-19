package com.iss.queue.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoctorQueue {
    private Doctor doctor;

    private List<Slot> slots;

    public List<Slot> returnOnlyAvailableSlots() {
        List<Slot> availableSlots = new ArrayList<>();
        for (Slot slot : slots) {
            if (slot.isAvailable()) {
                availableSlots.add(slot);
            }
        }
        return availableSlots;
    }
}
