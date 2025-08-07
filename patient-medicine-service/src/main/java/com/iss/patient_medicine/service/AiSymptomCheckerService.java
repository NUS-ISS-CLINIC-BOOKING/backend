package com.iss.patient_medicine.service;
import java.util.List;

public interface AiSymptomCheckerService {
    List<String> checkSymptoms(String symptoms);
}
