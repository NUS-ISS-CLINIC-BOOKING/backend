package com.iss.clinic.application;

import java.util.ArrayList;
import java.util.List;
import com.iss.clinic.domain.entity.Clinic;
import com.iss.clinic.domain.entity.Specialty;
import com.iss.clinic.dto.GetClinicInfoResult;
import com.iss.clinic.dto.GetClinicResult;
import com.iss.clinic.dto.GetSpecialtyListResult;
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
            return new GetClinicResult(new ArrayList<>(), e.getMessage());
        }
    }

    public GetClinicInfoResult getClinicInfo(int clinicId) {
        try {
            Clinic clinic = (Clinic) clinicRepository.findById(clinicId).orElse(null);
            if (clinic == null) {
                return new GetClinicInfoResult(null, "clinic not found");
            }
            return new GetClinicInfoResult(clinic, "get clinic information success");
        } catch (Exception e) {
            return new GetClinicInfoResult(null, e.getMessage());
        }
    }

    public GetSpecialtyListResult getClinicSpecialtyList(int clinicId) {
        try {
            List<Specialty> specialtyList = clinicRepository.findSpecialtyByClinicId(clinicId);
            if (specialtyList.isEmpty()) {
                return new GetSpecialtyListResult(new ArrayList<>(), "no specialties found");
            }
            return new GetSpecialtyListResult(specialtyList, "get specialties success");
        } catch (Exception e) {
            return new GetSpecialtyListResult(new ArrayList<>(), e.getMessage());
        }
    }
}