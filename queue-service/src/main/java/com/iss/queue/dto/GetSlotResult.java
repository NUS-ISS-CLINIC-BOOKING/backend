package com.iss.queue.dto;

import com.iss.queue.domain.entity.Slot;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetSlotResult {
    private List<Slot> slots;
    private String message;
}
