package com.ra.repository;

import com.ra.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findByProductNameContaining(String productName, Pageable pageable);
    Optional<Product> findByProductId(Long productId);
    Page<Product> findTop5ByOrderByCreatedAtDesc(Pageable pageable);
    Page<Product> findByCategoryId(Long categoryId, Pageable pageable);
}
