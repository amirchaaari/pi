package com.example.pi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:4200") // Your Angular app
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
                .allowedHeaders("*")
                .exposedHeaders("Authorization", "Content-Type") // Expose custom headers if needed
                .allowCredentials(true) // Important for sessions/cookies
                .maxAge(3600); // Cache preflight response for 1 hour

        // Add specific mapping for Stripe webhook if you have one
        registry.addMapping("/stripe-webhook/**")
                .allowedOrigins("https://api.stripe.com")
                .allowedMethods("POST");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:C:/Users/dalih/images/")
                .setCachePeriod(3600)
                .resourceChain(true);
    }
}