package com.iss.clinic.application;

import java.util.ArrayList;
import java.util.List;
import com.iss.clinic.domain.entity.Clinic;
import com.iss.clinic.dto.GetClinicResult;
import com.iss.clinic.repository.ClinicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClinicApplicationService {
    private final ClinicRepository clinicRepository;
    public GetClinicResult getAllClinics() {
        try {
            List<Clinic> clinics = clinicRepository.findAll();
            if (clinics.isEmpty()) {
                return new GetClinicResult(new ArrayList<>(), "no clinics found");
            }
            return new GetClinicResult(clinics, "get clinics success");
        } catch (Exception e) {
            return new GetClinicResult(new ArrayList<>(), "get clinics failed");
        }
    }
}