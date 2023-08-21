package com.tranthai.tranthaistore.converter;

import org.springframework.stereotype.Component;

import com.tranthai.tranthaistore.dto.CategoryDTO;
import com.tranthai.tranthaistore.model.Category;

@Component
public class CategoryConverter {
    public Category toEntity(CategoryDTO categoryDTO){
        Category category = new Category();
        category.setId(categoryDTO.getId());
        category.setName(categoryDTO.getName());
        return category;
    }

    public CategoryDTO toDTO(Category category){
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(category.getId());
        categoryDTO.setName(category.getName());
        return categoryDTO;
    }
}
