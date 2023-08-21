package com.tranthai.tranthaistore.service;

import java.util.List;
import java.util.Optional;

import com.tranthai.tranthaistore.model.Category;

public interface CategoryService {
    List<Category> getAllCategory();
    void addCategory(Category category);
    void removeCategoryById(Long id);
    Optional<Category> getCategoryById(long id);
    List<Category> searchCategory(String keyword);
}
