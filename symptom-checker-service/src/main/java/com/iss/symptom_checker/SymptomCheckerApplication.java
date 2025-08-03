package com.iss.symptom_checker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient // 启用 Nacos 服务发现
@EnableFeignClients // 启用 Feign 客户端
public class SymptomCheckerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SymptomCheckerApplication.class, args);
    }

}