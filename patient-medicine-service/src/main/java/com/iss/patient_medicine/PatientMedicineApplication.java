package com.iss.patient_medicine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;  // 启用 Nacos 服务发现
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient  // 必须添加此注解以注册到 Nacos
@EnableFeignClients
public class PatientMedicineApplication {
    public static void main(String[] args) {
        SpringApplication.run(PatientMedicineApplication.class, args);
    }
}