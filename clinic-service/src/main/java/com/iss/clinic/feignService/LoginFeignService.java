package com.iss.clinic.feignService;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name="login-service", path = "/login")
public interface LoginFeignService {

    @RequestMapping("/reduct")
    public String reduct();
}
