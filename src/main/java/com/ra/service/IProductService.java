package com.ra.service;

import com.ra.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface IProductService {
    Page<Product> getAllProducts(Pageable pageable);
    Page<Product> searchProductsByName(String productName, Pageable pageable);
    Optional<Product> getProductById(Long productId);
    Page<Product> getLatestProducts(Pageable pageable);
    Page<Product> getProductsByCategoryId(Long categoryId, Pageable pageable);

}
