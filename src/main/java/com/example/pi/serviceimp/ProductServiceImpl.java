package com.example.pi.serviceimp;

import com.example.pi.entity.Category;
import com.example.pi.entity.Product;
import com.example.pi.repository.CategoryRepository;
import com.example.pi.repository.ProductRepository;
import com.example.pi.service.ProductService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private final ProductRepository productRepository;


    @Autowired
    private final CategoryRepository categoryRepository; // Inject Category repository

    @Transactional
    public Product createProduct(Product product) {
        if (product.getCategory() != null && product.getCategory().getId() != null) {
            Category category = categoryRepository.findById(product.getCategory().getId())
                    .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + product.getCategory().getId()));
            product.setCategory(category);
        }
        return productRepository.save(product);
    }



    @Transactional
    public List<Product> getAllProducts() {
        return productRepository.findAll(); // Retrieve all products
    }

    @Transactional
    public Product getProductById(Long id) {
        Optional<Product> product = productRepository.findById(id);
        return product.orElse(null); // Return product or null if not found
    }

    @Transactional
    public Product updateProduct(Long id, Product productDetails) {
        // Retrieve existing product
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + id));

        // Validate product details
        if (productDetails.getName() == null || productDetails.getName().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be null or empty.");
        }
        if (productDetails.getPrice() == null || productDetails.getPrice() < 0) {
            throw new IllegalArgumentException("Product price must be a non-negative value.");
        }

        // Validate category first
        if (productDetails.getCategory() != null && productDetails.getCategory().getId() != null) {
            Category category = categoryRepository.findById(productDetails.getCategory().getId())
                    .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + productDetails.getCategory().getId()));
            product.setCategory(category);
        }

        // Update fields only if category is valid
        product.setName(productDetails.getName());
        product.setDescription(productDetails.getDescription());
        product.setPrice(productDetails.getPrice());

        // Save updated product
        return productRepository.save(product);
    }

    @Override
    public void deleteProduct(Long id) {

    }


    @Transactional
    public void deleteCommandsByProductId(Long productId) {
        // Assuming you have a CommandRepository to handle command entities


    }

    @Override
    public List<Product> getProductsByCategory(Long categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }

    @Transactional
    public List<Product> getProductsByCategoryAndPrice(Long categoryId, Double price) {
        return productRepository.findByCategoryIdAndPriceLessThan(categoryId, price);
    }
}