package com.iss.patient_medicine.controller;

import com.stripe.Stripe;
import com.stripe.model.PaymentIntent;
import com.stripe.exception.StripeException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    // 创建 PayNow 支付订单
    @PostMapping("/paynow/create")
    public ResponseEntity<?> createPayNowPayment(@RequestParam("appointmentId") String appointmentId) {
        try {
            Stripe.apiKey = stripeSecretKey;

            // 创建支付参数
            Map<String, Object> params = new HashMap<>();
            params.put("amount", 10000); // 以分为单位（100.00 SGD）
            params.put("currency", "sgd");
            params.put("payment_method_types", List.of("paynow"));

            // 设置 metadata（用于 webhook 回调识别订单）
            Map<String, String> metadata = new HashMap<>();
            metadata.put("appointment_id", appointmentId);
            params.put("metadata", metadata);

            // 创建支付意图
            PaymentIntent intent = PaymentIntent.create(params);

            // 提取二维码页面链接
            String qrCodeUrl = null;
            if (intent.getNextAction() != null && intent.getNextAction().getPaynowDisplayQrCode() != null) {
                qrCodeUrl = intent.getNextAction().getPaynowDisplayQrCode().getHostedInstructionsUrl();
            }

            Map<String, String> result = new HashMap<>();
            result.put("paymentIntentId", intent.getId());
            result.put("qrCodeUrl", qrCodeUrl != null ? qrCodeUrl : "QR code unavailable");

            return ResponseEntity.ok(result);

        } catch (StripeException e) {
            return ResponseEntity.status(500).body("Stripe error: " + e.getMessage());
        }
    }
}
