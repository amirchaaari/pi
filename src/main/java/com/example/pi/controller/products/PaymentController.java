package com.example.pi.controller.products;

import com.example.pi.entity.CartItemRequest;
import com.example.pi.service.serviceimpProducts.StripeService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {
    @Autowired
    private final StripeService stripeService;

    @PostMapping("/create-session")
    public ResponseEntity<?> createCheckoutSession(
            @RequestBody List<CartItemRequest> cartItems,
            HttpServletRequest request) {

        System.out.println("Received cart items: " + cartItems);
        System.out.println("Headers: " + request.getHeaderNames());

        try {
            String baseUrl = request.getScheme() + "://" + request.getServerName() +
                    (request.getServerPort() != 80 ? ":" + request.getServerPort() : "");
            System.out.println("Base URL: " + baseUrl);

            String sessionId = stripeService.createSession(cartItems, baseUrl);
            return ResponseEntity.ok(Map.of("sessionId", sessionId));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500)
                    .body(Map.of("error", e.toString()));
        }
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> healthCheck() {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of("status", "Payment service is healthy"));
    }



}