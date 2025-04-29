package com.example.pi.entity;

import lombok.Data;

@Data
public class CartItemRequest {
    private String id;
    private String name;
    private double price;
    private long quantity;
    private String imageUrl; // Add this field
}
