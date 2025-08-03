package com.iss.symptom_checker.domain.service;

import java.util.List;

public interface AiSymptomCheckerService {
    List<String> checkSymptoms(String symptoms);
}