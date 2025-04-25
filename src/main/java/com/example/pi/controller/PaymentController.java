//package com.example.pi.controller;
//
//import com.example.pi.entity.CartItemRequest;
//import com.example.pi.serviceimp.StripeService;
//import com.stripe.exception.StripeException;
//import jakarta.servlet.http.HttpServletRequest;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.List;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/api/payments")
//@RequiredArgsConstructor
//public class PaymentController {
//    @Autowired
//    private final StripeService stripeService;
//
//    @PostMapping("/create-session")
//    public ResponseEntity<Map<String, String>> createCheckoutSession(
//            @RequestBody List<CartItemRequest> cartItems,
//            HttpServletRequest request) {
//
//        String baseUrl = request.getRequestURL().toString()
//                .replace(request.getRequestURI(), request.getContextPath());
//
//        try {
//            String sessionId = stripeService.createSession(cartItems, baseUrl);
//            return ResponseEntity.ok(Map.of("sessionId", sessionId));
//        } catch (StripeException e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(Map.of("error", e.getMessage()));
//        }
//    }
//}