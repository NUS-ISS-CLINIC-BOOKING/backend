package com.iss.patient_medicine.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/patient_medicine")
public class PatientMedicineController {
    @GetMapping("/status")
    public String getPatientMedicineStatus() {
        return "Patient_Medicine service is running!";
    }
}