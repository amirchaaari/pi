package com.example.pi.serviceimp;

import com.example.pi.entity.Category;
import com.example.pi.entity.Product;
import com.example.pi.entity.ProductStatistics;
import com.example.pi.repository.CategoryRepository;
import com.example.pi.repository.ProductRepository;
import com.example.pi.service.ProductService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {


    private static final int LOW_STOCK_THRESHOLD = 5;

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
    public Product updateProduct(Long id, String name, String description,
                                 Double price, Integer quantity,
                                 Long categoryId, MultipartFile image) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        // Update basic fields
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setQuantity(quantity);

        // Update category if changed
        if (categoryId != null && !categoryId.equals(product.getCategory().getId())) {
            Category newCategory = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new EntityNotFoundException("Category not found"));
            product.setCategory(newCategory);
        }

        // Handle image update
        if (image != null && !image.isEmpty()) {
            String imageUrl = saveImage(image);
            product.setImageUrl(imageUrl);
        }

        return productRepository.save(product);
    }
    @Override
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new EntityNotFoundException("Category not found with id: " + id);
        }
        productRepository.deleteById(id); // Delete the category by ID

    }


    @Transactional
    public void deleteCommandsByProductId(Long productId) {
        // Assuming you have a CommandRepository to handle command entities


    }



    @Override
    public List<Product> getProductsByCategory(Long categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }

    @Override
    public Product updateProduct(Long id, Product productDetails) {
        return null;
    }

    @Transactional
    public List<Product> getProductsByCategoryAndPrice(Long categoryId, Double price) {
        return productRepository.findByCategoryIdAndPriceLessThan(categoryId, price);
    }

    public String saveImage(MultipartFile imageFile) {
        try {
            String directoryPath = "C:/Users/dalih/images/products"; // Ensure this path is correct
            String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
            File targetFile = new File(directoryPath, fileName);
            imageFile.transferTo(targetFile);

            // Log the complete URL
            String imageUrl = "/images/products/" + fileName; // Adjust according to your server's URL structure
            System.out.println("Image URL: " + imageUrl);
            return imageUrl;
        } catch (IOException e) {
            e.printStackTrace();
            return null; // Handle the error appropriately
        }
    }

    @Autowired
    private NotificationService notificationService;

    @Scheduled(fixedRate = 10000) // Check every minute
    public void checkLowStockProducts() {
        List<Product> lowStockProducts = productRepository.findByQuantityLessThan(LOW_STOCK_THRESHOLD);

        if (!lowStockProducts.isEmpty()) {
            for (Product product : lowStockProducts) {
                notificationService.sendLowStockNotification(
                        product.getName(),
                        product.getQuantity()
                );
            }

            // Also log it
            String logMessage = "Low stock notification sent for: " +
                    lowStockProducts.stream()
                            .map(p -> p.getName() + " (" + p.getQuantity() + " left)")
                            .collect(Collectors.joining(", "));
            System.out.println(logMessage);
        }
    }

    @Scheduled(fixedRate = 10000) // 60 seconds
    public void logTotalProductCount() {
        long productCount = productRepository.count();
        System.out.println("Total number of products in inventory: " + productCount);
    }

    @Transactional
    public ProductStatistics getProductStatistics() {
        ProductStatistics stats = new ProductStatistics();
        stats.setTotalProducts(productRepository.count());
        stats.setTotalQuantity(productRepository.sumQuantities());
        stats.setAveragePrice(productRepository.averagePrice());
        stats.setLowStockCount(productRepository.countLowStockProducts(LOW_STOCK_THRESHOLD));
        // Implement this if needed
        return stats;
    }



}