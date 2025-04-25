package com.example.pi.serviceimp;

import com.example.pi.entity.CartItemRequest;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StripeService {

    @Value("${stripe.secret.key}")
    private String secretKey;

    public String createSession(List<CartItemRequest> cartItems, String baseUrl)
            throws StripeException {

        // Initialize Stripe with secret key
        Stripe.apiKey = secretKey;

        List<SessionCreateParams.LineItem> lineItems = cartItems.stream()
                .map(item -> SessionCreateParams.LineItem.builder()
                        .setPriceData(
                                SessionCreateParams.LineItem.PriceData.builder()
                                        .setCurrency("usd")
                                        .setProductData(
                                                SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                        .setName(item.getName())
                                                        .build())
                                        .setUnitAmount((long)(item.getPrice() * 100))
                                        .build())
                        .setQuantity(item.getQuantity())
                        .build())
                .collect(Collectors.toList());

        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(baseUrl + "/checkout/success?session_id={CHECKOUT_SESSION_ID}")
                .setCancelUrl(baseUrl + "/checkout/cancel")
                .addAllLineItem(lineItems)
                .build();

        Session session = Session.create(params);
        return session.getId();
    }
}