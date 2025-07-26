package com.iss.patient_medicine.controller;

import com.stripe.Stripe;
import com.stripe.model.PaymentIntent;
import com.stripe.exception.StripeException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.stripe.model.checkout.Session;

import java.util.*;

@RestController
@RequestMapping("/api/patient_medicine")
public class PatientMedicineController {

    // 你可以把这个 secret 配置到 application.yml 或 .env 中
    @Value("${stripe.secret-key}")
    private String stripeSecretKey;

    // 服务健康检查
    @GetMapping("/status")
    public String getPatientMedicineStatus() {
        return "Patient_Medicine service is running!";
    }

    @PostMapping("/paynow/create")
    public ResponseEntity<?> createCheckoutSession(@RequestParam("appointmentId") String appointmentId) {
        try {
            Stripe.apiKey = stripeSecretKey;

            // 回调链接
            String successUrl = "https://your-clinic.com/payment-success?session_id={CHECKOUT_SESSION_ID}";
            String cancelUrl = "https://your-clinic.com/payment-cancel";

            // 创建 Checkout Session 参数
            Map<String, Object> params = new HashMap<>();
            params.put("payment_method_types", List.of("paynow"));
            params.put("mode", "payment");
            params.put("success_url", successUrl);
            params.put("cancel_url", cancelUrl);

            // 商品信息（可以理解为 appointment 支付 5 新币）
            Map<String, Object> lineItem = new HashMap<>();
            lineItem.put("quantity", 1);

            Map<String, Object> priceData = new HashMap<>();
            priceData.put("currency", "sgd");
            priceData.put("unit_amount", 500); // 单位是分
            priceData.put("product_data", Map.of("name", "Appointment Payment"));

            lineItem.put("price_data", priceData);
            params.put("line_items", List.of(lineItem));

            // Metadata 用于 webhook 识别
            params.put("metadata", Map.of("appointment_id", appointmentId));

            Session session = Session.create(params);

            return ResponseEntity.ok(Map.of("checkoutUrl", session.getUrl()));

        } catch (StripeException e) {
            return ResponseEntity.status(500).body("Stripe error: " + e.getMessage());
        }
    }



}
