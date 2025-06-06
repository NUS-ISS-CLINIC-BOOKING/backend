package com.iss.queue.common.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SuccessResponse<T> {
    private int code = 200;            // Success code, always 200
    private String message = "OK";     // Default success message
    private T data;                    // Actual data payload

    public SuccessResponse(T data) {
        this.data = data;
    }
}
