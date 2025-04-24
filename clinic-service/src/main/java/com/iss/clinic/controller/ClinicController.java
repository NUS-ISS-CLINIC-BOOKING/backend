package com.iss.clinic.controller;

import com.iss.clinic.feignService.LoginFeignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/clinic")
public class ClinicController {
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
}