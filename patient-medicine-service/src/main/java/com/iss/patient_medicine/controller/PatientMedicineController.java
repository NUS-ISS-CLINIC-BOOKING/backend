package com.iss.patient_medicine.controller;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/patient_medicine")
public class PatientMedicineController {

    @Value("${stripe.secret-key}")
    private String stripeSecretKey;

    // 服务健康检查
    @GetMapping("/status")
    public String getPatientMedicineStatus() {
        return "Patient_Medicine service is running!";
    }

    // 创建 PayNow Checkout Session
    @PostMapping("/paynow/create")
    public ResponseEntity<?> createCheckoutSession(@RequestParam("appointmentId") String appointmentId) {
        try {
            Stripe.apiKey = stripeSecretKey;

            String successUrl = "https://your-clinic.com/payment-success?session_id={CHECKOUT_SESSION_ID}";
            String cancelUrl = "https://your-clinic.com/payment-cancel";

            Map<String, Object> priceData = Map.of(
                    "currency", "sgd",
                    "unit_amount", 500,
                    "product_data", Map.of("name", "Appointment Payment")
            );

            Map<String, Object> lineItem = Map.of(
                    "quantity", 1,
                    "price_data", priceData
            );

            Map<String, Object> params = Map.of(
                    "payment_method_types", List.of("paynow"),
                    "mode", "payment",
                    "success_url", successUrl,
                    "cancel_url", cancelUrl,
                    "line_items", List.of(lineItem),
                    "metadata", Map.of("appointment_id", appointmentId)
            );

            Session session = Session.create(params);
            return ResponseEntity.ok(Map.of("checkoutUrl", session.getUrl()));

        } catch (StripeException e) {
            return ResponseEntity.status(500).body("Stripe error: " + e.getMessage());
        }
    }

    // ✅ Webhook 回调 - 不用 servlet，适合开发/调试环境
    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(@RequestBody Map<String, Object> body) {
        System.out.println("✅ Webhook received");

        String eventType = (String) body.get("type");

        if (!"checkout.session.completed".equals(eventType)) {
            return ResponseEntity.ok("Ignored event type: " + eventType);
        }
        System.out.println("✅ 支付完成");
        return ResponseEntity.ok("OK");
    }
}
