package com.example.pi.service;

import com.example.pi.entity.Product;
import com.example.pi.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Product getProductById(Long id) {
        Optional<Product> product = productRepository.findById(id);
        return product.orElse(null); // Handle case where product is not found
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll(); // This should work if JpaRepository is set up correctly
    }

    @Override
    public Product updateProduct(Long id, Product productDetails) {
        if (productRepository.existsById(id)) {
            productDetails.setId(id); // Set the ID to ensure correct update
            return productRepository.save(productDetails);
        }
        return null; // Or throw an exception if needed
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}