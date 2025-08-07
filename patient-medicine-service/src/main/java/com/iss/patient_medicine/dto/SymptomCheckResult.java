package com.iss.patient_medicine.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class SymptomCheckResult {
    private List<String> possibleConditions;
    private String recommendation;

    public SymptomCheckResult(List<String> possibleConditions, String recommendation) {
        this.possibleConditions = possibleConditions;
        this.recommendation = recommendation;
    }

    // ... existing code ...

    // 确保有 getter 和 setter 方法
    public List<String> getPossibleConditions() {
        return possibleConditions;
    }

    public void setPossibleConditions(List<String> possibleConditions) {
        this.possibleConditions = possibleConditions;
    }

    public String getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(String recommendation) {
        this.recommendation = recommendation;
    }

}