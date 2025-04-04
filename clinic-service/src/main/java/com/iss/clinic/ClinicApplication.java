package com.iss.clinic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;  // 启用 Nacos 服务发现

@SpringBootApplication
@EnableDiscoveryClient  // 必须添加此注解以注册到 Nacos
public class ClinicApplication {
    public static void main(String[] args) {
        SpringApplication.run(ClinicApplication.class, args);
    }
}