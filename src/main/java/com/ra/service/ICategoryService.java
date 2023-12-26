package com.ra.service;

import com.ra.model.Category;
import com.ra.util.exception.AppException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ICategoryService {
    List<Category> getAllCategories();
    Page<Category> getAllCategories(Pageable pageable);
    Optional<Category> getCategoryById(Long categoryId);
    void addCategory(Category category) throws AppException;
    boolean updateCategory(Long categoryId, Category updatedCategory);
    boolean deleteCategory(Long categoryId);

}
