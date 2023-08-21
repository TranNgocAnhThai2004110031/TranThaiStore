package com.tranthai.tranthaistore.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranthai.tranthaistore.model.Category;
import com.tranthai.tranthaistore.repository.CategoryRepository;
import com.tranthai.tranthaistore.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService{
    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<Category> getAllCategory() {
        return this.categoryRepository.findAll();
    }

    @Override
    public void addCategory(Category category) {
        this.categoryRepository.save(category);    
    }

    @Override
    public void removeCategoryById(Long id) {
        this.categoryRepository.deleteById(id);    
    }

    @Override
    public Optional<Category> getCategoryById(long id) {
        return this.categoryRepository.findById(id);    
    }

    @Override
    public List<Category> searchCategory(String keyword) {
        return this.categoryRepository.findByNameContainingIgnoreCase(keyword);    
    }
    
}
