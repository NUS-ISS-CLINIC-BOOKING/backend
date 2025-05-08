package com.iss.clinic.dto;

import java.util.ArrayList;
import java.util.List;

import com.iss.clinic.domain.entity.Clinic;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetClinicResult {
    private List<Clinic> clinics;
    private String message;
}
