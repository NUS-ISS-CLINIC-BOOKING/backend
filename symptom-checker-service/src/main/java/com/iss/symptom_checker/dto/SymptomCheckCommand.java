package com.iss.symptom_checker.dto;

import jakarta.validation.constraints.NotBlank;

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