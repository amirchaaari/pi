package com.example.pi.entity;

import java.util.List;

public class ProductStatistics {
    private long totalProducts;
    private long totalQuantity;
    private double averagePrice;
    private long lowStockCount;
    private List<Product> topSellingProducts;

    // Getters and Setters
    public long getTotalProducts() {
        return totalProducts;
    }

    public void setTotalProducts(long totalProducts) {
        this.totalProducts = totalProducts;
    }

    public long getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(long totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public double getAveragePrice() {
        return averagePrice;
    }

    public void setAveragePrice(double averagePrice) {
        this.averagePrice = averagePrice;
    }

    public long getLowStockCount() {
        return lowStockCount;
    }

    public void setLowStockCount(long lowStockCount) {
        this.lowStockCount = lowStockCount;
    }

    public List<Product> getTopSellingProducts() {
        return topSellingProducts;
    }

    public void setTopSellingProducts(List<Product> topSellingProducts) {
        this.topSellingProducts = topSellingProducts;
    }
}
