package com.tranthai.tranthaistore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tranthai.tranthaistore.converter.ProductConverter;
import com.tranthai.tranthaistore.dto.ProductDTO;
import com.tranthai.tranthaistore.model.Product;
import com.tranthai.tranthaistore.service.BrandService;
import com.tranthai.tranthaistore.service.CategoryService;
import com.tranthai.tranthaistore.service.ProductService;

@Controller
public class ProductController {
    @Autowired
    private ProductService productService;

    @Autowired 
    private CategoryService categoryService;

    @Autowired 
    private ProductConverter productConverter;

    @Autowired
    private BrandService brandService;

    @GetMapping("/admin/product/add")
    public String getProduct(Model model) {
        model.addAttribute("productDTO", new ProductDTO());
        model.addAttribute("categories", this.categoryService.getAllCategory());
        model.addAttribute("brands", this.brandService.getAllBrand());
        return "productAdd";
    }

    @PostMapping("/admin/product/add")
    public String addProduct(@ModelAttribute("productDTO") ProductDTO productDTO) {
        Product product = this.productConverter.toEntity(productDTO);        
        this.productService.addProduct(product);
        return "redirect:/admin/products";
    }

    @GetMapping("/admin/product/{id}")
    public String getProductById(@PathVariable Long id, Model model){
        ProductDTO productDTO = this.productConverter.toDTO(this.productService.getProductById(id).get());
        model.addAttribute("productDTO", productDTO);
        model.addAttribute("categories", this.categoryService.getAllCategory());        
        model.addAttribute("brands", this.brandService.getAllBrand());

        return "productAdd";
    }

    @GetMapping("/admin/product/delete/{id}")
    public String updateProduct(@PathVariable("id") Long id){
        this.productService.removeProductById(id);
        return "redirect:/admin/products";
    }
    
    @GetMapping("/admin/viewproduct/{id}")
    public String viewAdminProduct(@PathVariable("id") Long id, Model model){
        model.addAttribute("product", this.productService.getProductById(id).get());
        return "viewProductAdmin";
    }

    @GetMapping("/admin/products/search")
    public String searchProduct(@RequestParam("keyword") String keyword, RedirectAttributes redirectAttributes){
        // redirectAttributes.addFlashAttribute("products", this.productService.searchProduct(keyword));
        redirectAttributes.addAttribute("keyword", keyword);
        return "redirect:/admin/products";
    }
    
}
