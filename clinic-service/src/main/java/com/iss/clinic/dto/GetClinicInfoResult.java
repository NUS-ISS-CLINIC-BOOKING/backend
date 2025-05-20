package com.iss.clinic.dto;

import com.iss.clinic.domain.entity.Clinic;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetClinicInfoResult {
    private Clinic clinic;
    private String message;
}
