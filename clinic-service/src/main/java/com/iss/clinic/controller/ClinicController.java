package com.iss.clinic.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/clinic")
public class ClinicController {
    @GetMapping("/status")
    public String getClinicStatus() {
        return "Clinic service is running!";
    }
}