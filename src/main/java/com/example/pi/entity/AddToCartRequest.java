package com.example.pi.entity;

public class AddToCartRequest {
    private Long productId;
    private Integer quantity;

    // Getters and setters
    public Long getProductId() { return productId; }
    public Integer getQuantity() { return quantity; }
    public void setProductId(Long productId) { this.productId = productId; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
}