package com.iss.patient_medicine.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SymptomCheckCommand {
    @NotBlank(message = "症状描述不能为空")
    private String symptoms;

    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }
}