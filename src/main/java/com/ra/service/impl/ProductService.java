package com.ra.service.impl;

import com.ra.model.Product;
import com.ra.repository.IProductRepository;
import com.ra.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductService implements IProductService {
    @Autowired
    private IProductRepository productRepository;

    @Override
    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    @Override
    public Page<Product> searchProductsByName(String productName, Pageable pageable) {
        return productRepository.findByProductNameContaining(productName, pageable);
    }

    @Override
    public Optional<Product> getProductById(Long productId) {
        return productRepository.findByProductId(productId);
    }

    @Override
    public Page<Product> getLatestProducts(Pageable pageable) {
        return productRepository.findTop5ByOrderByCreatedAtDesc(pageable);
    }

    @Override
    public Page<Product> getProductsByCategoryId(Long categoryId, Pageable pageable) {
        return productRepository.findByCategoryId(categoryId, pageable);
    }
}
