package com.iss.auth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/login")
public class LoginController {

//    @GetMapping("/login")
//    public String login() {
//        return "Login successful!";
//    }
    @RequestMapping("/reduct")
    public String reduct() throws InterruptedException{
        TimeUnit.SECONDS.sleep(1);
        System.out.println("扣减库存");
        return "扣减库存";
    }
}

