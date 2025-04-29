package com.example.pi.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockNotification {
    private String type = "STOCK_ALERT";
    private String message;
    private LocalDateTime timestamp;
    private List<Product> products;

    // Constructors, getters, setters
}