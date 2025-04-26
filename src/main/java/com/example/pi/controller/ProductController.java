package com.example.pi.controller;

import com.example.pi.entity.Category;
import com.example.pi.entity.Product;
import com.example.pi.entity.ProductStatistics;
import com.example.pi.repository.CategoryRepository;
import com.example.pi.service.ProductService;
import com.example.pi.serviceimp.NotificationService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired  // Make sure this is present
    private NotificationService notificationService;  // Instance field


    // Ensure this is correct
    @GetMapping("/category/{categoryId}")
//        @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<List<Product>> getProductsByCategory(@PathVariable Long categoryId) {
        List<Product> products = productService.getProductsByCategory(categoryId);
        return ResponseEntity.ok(products);
    }
    @GetMapping
//        @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createProduct(
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam Double price,
            @RequestParam Long categoryId,
            @RequestParam Integer quantity,
            @RequestParam(required = false) MultipartFile image) {

        try {
            Product product = new Product();
            product.setName(name);
            product.setDescription(description);
            product.setPrice(price);
            product.setQuantity(quantity);

            // Set category
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new EntityNotFoundException("Category not found"));
            product.setCategory(category);

            // Handle image upload if present
            if (image != null && !image.isEmpty()) {
                String imageUrl = productService.saveImage(image); // Implement uploadImage method
                product.setImageUrl(imageUrl);
            }

            Product createdProduct = productService.createProduct(product);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Category not found");
        } catch (Exception e) {
            e.printStackTrace(); // Log the exception
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating product: " + e.getMessage());
        }
    }
    @GetMapping("/{id}")
//        @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        return product != null ? ResponseEntity.ok(product) : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//        @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Product> updateProduct(
            @PathVariable Long id,
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam Double price,
            @RequestParam Integer quantity,
            @RequestParam Long categoryId,  // Make sure this matches your frontend
            @RequestParam(required = false) MultipartFile imageFile) {

        // Debug output
        System.out.println("Received categoryId: " + categoryId);

        // Your update logic
        Product updatedProduct = productService.updateProduct(id, name, description,
                price, quantity, categoryId, imageFile);
        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("/{id}")
//        @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/category/{categoryId}/price/{price}")
//        @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<List<Product>> getProductsByCategoryAndPrice(
            @PathVariable Long categoryId,
            @PathVariable Double price) {
        List<Product> products = productService.getProductsByCategoryAndPrice(categoryId, price);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/statistics")
//        @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ProductStatistics getProductStatistics() {
        return productService.getProductStatistics();
    }

    @GetMapping("/test-notification")
//        @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void testNotification() {
        notificationService.sendGlobalNotification("WebSocket test successful!");
    }
}