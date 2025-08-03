package com.iss.symptom_checker.application;

import com.iss.symptom_checker.domain.service.AiSymptomCheckerService;
import com.iss.symptom_checker.dto.SymptomCheckCommand;
import com.iss.symptom_checker.dto.SymptomCheckResult;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class SymptomCheckerApplicationService {

    private final AiSymptomCheckerService aiSymptomCheckerService;

    public SymptomCheckerApplicationService(AiSymptomCheckerService aiSymptomCheckerService) {
        this.aiSymptomCheckerService = aiSymptomCheckerService;
    }

    public SymptomCheckResult checkSymptoms(SymptomCheckCommand command) {
        try {
            List<String> specialties = aiSymptomCheckerService.checkSymptoms(command.getSymptoms());
            return new SymptomCheckResult(specialties, "症状分析成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new SymptomCheckResult(Collections.emptyList(), "分析症状时发生错误: " + e.getMessage());
        }
    }
}