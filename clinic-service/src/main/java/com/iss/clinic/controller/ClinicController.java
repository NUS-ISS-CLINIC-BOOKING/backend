package com.iss.clinic.controller;

import com.iss.clinic.application.ClinicApplicationService;
import com.iss.clinic.common.response.SuccessResponse;
import com.iss.clinic.dto.GetClinicInfoResult;
import com.iss.clinic.dto.GetClinicResult;
import com.iss.clinic.feignService.LoginFeignService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/clinic")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ClinicController {

    private final ClinicApplicationService clinicApplicationService;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    LoginFeignService loginFeignService;

    @RequestMapping("/add")
    public String add(){
        System.out.println("下单成功！");
//        String msg = restTemplate.getForObject("http://login-service/login", String.class);
        String msg = loginFeignService.reduct();
        return "Hello World +"+msg;
    }
//    @GetMapping("/status")
//    public String getClinicStatus() {
//        return "Clinic service is running!";
//    }
    @GetMapping("/all")
    public SuccessResponse<GetClinicResult> getAllClinics() {
        return new SuccessResponse<>(clinicApplicationService.getAllClinics());
    }

    @GetMapping("/location/{clinicId}")
    public SuccessResponse<GetClinicInfoResult> getClinicLocation(@PathVariable int clinicId) {
        return new SuccessResponse<>(clinicApplicationService.getClinicInfo(clinicId));
    }
}