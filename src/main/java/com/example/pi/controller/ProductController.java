package com.example.pi.controller;

import com.example.pi.entity.Product;
import com.example.pi.serviceimp.ProductServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductServiceImpl productServiceImpl; // Fix variable name to follow naming conventions

    @GetMapping
    public List<Product> getAllProducts() {
        return productServiceImpl.getAllProducts(); // Call the correct method
    }

    @PostMapping
    public Product createProduct(@RequestBody Product product) {
        return productServiceImpl.createProduct(product); // Call the correct method
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productServiceImpl.deleteProduct(id); // Call the correct method
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}