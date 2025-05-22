package com.iss.clinic.dto;

import com.iss.clinic.domain.entity.Specialty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetSpecialtyListResult {
    private List<Specialty> specialtyList;
    private String message;
}
