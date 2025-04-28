package com.example.pi.service;

import com.example.pi.entity.Product;
import com.example.pi.entity.ProductStatistics;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {
    Product createProduct(Product product);



    Product getProductById(Long id);
    List<Product> getAllProducts(); // Matche
    List<Product> getProductsByCategory(Long categoryId);// s with the controller
    Product updateProduct(Long id, Product productDetails); // Should be called in the controller
    void deleteProduct(Long id); // Should be called in the controller
    void deleteCommandsByProductId(Long productId); // New method
    List<Product> getProductsByCategoryAndPrice(Long categoryId, Double price);
    String saveImage(MultipartFile imageFile);

    Product updateProduct(Long id, String name, String description, Double price, Integer quantity, Long categoryId, MultipartFile image);
    // Add this method
    ProductStatistics getProductStatistics();
}