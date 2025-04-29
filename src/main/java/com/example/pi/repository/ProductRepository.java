package com.example.pi.repository;

import com.example.pi.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // Additional query methods can be defined here if needed


    List<Product> findByCategoryName(String name);
    List<Product> findByCategoryId(Long categoryId);
    List<Product> findByNameContainingIgnoreCase(String name);
    List<Product> findByCategoryIdAndPriceLessThan(Long categoryId, Double price);
///schedule
    List<Product> findByQuantityLessThan(int quantity);


    @Query("SELECT SUM(p.quantity) FROM Product p")
    Long sumQuantities();

    @Query("SELECT AVG(p.price) FROM Product p")
    Double averagePrice();

    @Query("SELECT COUNT(p) FROM Product p WHERE p.quantity < :threshold")
    Long countLowStockProducts(@Param("threshold") int threshold);

//    @Query("SELECT p FROM Product p ORDER BY p.sales DESC") // Example for top-selling products
//    List<Product> findTopSellingProducts(Pageable pageable); // Adjust as needed
}