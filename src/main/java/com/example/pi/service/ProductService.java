package com.example.pi.service;

import com.example.pi.entity.Product;
import java.util.List;

public interface ProductService {
    Product createProduct(Product product);
    Product getProductById(Long id);
    List<Product> getAllProducts(); // Matches with the controller
    Product updateProduct(Long id, Product product); // Should be called in the controller
    void deleteProduct(Long id);
}