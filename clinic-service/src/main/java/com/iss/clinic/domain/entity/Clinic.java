package com.iss.clinic.domain.entity;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Clinic {
    private Integer id;
    private String name;
    private String address;
    private String phone;
    private Double longitude;
    private Double latitude;
    private Integer staff_list_id;


    //TODO
    public void calculateDistance(Clinic clinic) {
        // 实现逻辑
        System.out.println("calculateDistance");
    }
}