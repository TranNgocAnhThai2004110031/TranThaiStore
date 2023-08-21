package com.tranthai.tranthaistore.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tranthai.tranthaistore.converter.CategoryConverter;
import com.tranthai.tranthaistore.dto.CategoryDTO;
import com.tranthai.tranthaistore.model.Category;
import com.tranthai.tranthaistore.service.CategoryService;

@Controller
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @Autowired 
    private CategoryConverter categoryConverter;

    @GetMapping("/admin/category/add")
    public String getCategory(Model model){
        model.addAttribute("categoryDTO", new CategoryDTO());
        return "categoriesAdd";
    }

    @PostMapping("/admin/category/add")
    public String addCategory(@ModelAttribute("categoruDTO") CategoryDTO categoryDTO){
        this.categoryService.addCategory(this.categoryConverter.toEntity(categoryDTO));
        return "redirect:/admin/categories";
    }

    @GetMapping("/admin/category/delete/{id}")
    public String deleteCategory(@PathVariable("id") Long id){
        this.categoryService.removeCategoryById(id);
        return "redirect:/admin/categories";
    }

    @GetMapping("/admin/category/{id}")
    public String getCategoryById(@PathVariable int id, Model model) {
        Category category = categoryService.getCategoryById(id).get();
        model.addAttribute("category", category);
        return "categoriesAdd";
    }

    @GetMapping("/categories/search")
    public String searchCategory(@RequestParam String keyword, RedirectAttributes redirectAttributes) {
        List<Category> results = categoryService.searchCategory(keyword);
        redirectAttributes.addFlashAttribute("categories", results);

        return "categories";
    }

}
