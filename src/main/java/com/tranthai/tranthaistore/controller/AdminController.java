package com.tranthai.tranthaistore.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.tranthai.tranthaistore.model.Bill;
import com.tranthai.tranthaistore.model.Brand;
import com.tranthai.tranthaistore.model.Category;
import com.tranthai.tranthaistore.model.Product;
import com.tranthai.tranthaistore.model.User;
import com.tranthai.tranthaistore.service.BillService;
import com.tranthai.tranthaistore.service.BrandService;
import com.tranthai.tranthaistore.service.CategoryService;
import com.tranthai.tranthaistore.service.ProductService;
import com.tranthai.tranthaistore.service.UserService;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private CategoryService categoryService;  

    @Autowired 
    private ProductService productService; 

    @Autowired
    private UserService userService;

    @Autowired
    private BillService billService;

    @Autowired
    private BrandService brandService;

    @GetMapping()
    public String adminHome(){
        return "adminHome";
    }

    @GetMapping("/categories")
    public String getCategories(Model model){
        // model.addAttribute("categories", this.categoryService.getAllCategory());
        List<Category> categories = (List<Category>) model.getAttribute("categories");
        if (categories == null) {
            categories = this.categoryService.getAllCategory();
            model.addAttribute("categories", categories);
        }
        return "categories";
    }

    // @GetMapping("/products")
    // public String getProducts(@RequestParam(value = "page", required = false, defaultValue = "1") Integer page, Model model){
    //     // model.addAttribute("products", this.productService.getAllProduct());       
    //     if (page <= 0) {
    //         page = 1;
    //     }
    //     List<Product> products = (List<Product>) model.getAttribute("products");
    //     if (products == null) {
    //         // products = this.productService.getAllProduct();
    //         Pageable pageable = PageRequest.of(page - 1, 10);
    //         Page<Product> productPage = this.productService.getAllProductPage(pageable);
    //         model.addAttribute("products", productPage.getContent());
    //         model.addAttribute("currentPage", page);
    //         model.addAttribute("totalPages", productPage.getTotalPages());
    //     }
    //     return "products";
    // }
    @GetMapping("/products")
    public String getProducts(@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                            @RequestParam(value = "keyword", required = false) String keyword,
                            Model model) {
        if (page <= 0) {
            page = 1;
        }
        
        List<Product> products = (List<Product>) model.getAttribute("products");
        if (products == null || keyword != null) {
            Pageable pageable = PageRequest.of(page - 1, 10);
            Page<Product> productPage;

            if (keyword != null) {
                productPage = this.productService.searchProductPage(keyword, pageable);
            } else {
                productPage = this.productService.getAllProductPage(pageable);
            }

            model.addAttribute("products", productPage.getContent());
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", productPage.getTotalPages());
        }
        
        return "products";
    }


    @GetMapping("/users")
    public String getUsers(Model model){
        // model.addAttribute("users", this.userService.getAllUser());
        List<User> users = (List<User>) model.getAttribute("users");
        if (users == null) {
            users = this.userService.getAllUser();
            model.addAttribute("users", users);
        }
        return "users";
    }

    @GetMapping("/bills")
    public String getBills(Model model){
        // model.addAttribute("bills", this.billService.getAllBill());
        List<Bill> bills = (List<Bill>) model.getAttribute("bills");
        if (bills == null) {
            bills = this.billService.getAllBill();
            model.addAttribute("bills", bills);
        }
        return "bills";
    }

    @GetMapping("/brands")
    public String getBrand(Model model){
        List<Brand> brands = (List<Brand>) model.getAttribute("brand");
        if (brands == null) {
            brands = this.brandService.getAllBrand();
            model.addAttribute("brands", brands);
        }
        return "brands";
    }
    
}
