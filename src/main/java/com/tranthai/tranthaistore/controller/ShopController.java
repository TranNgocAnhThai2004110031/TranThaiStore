package com.tranthai.tranthaistore.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tranthai.tranthaistore.model.Product;
import com.tranthai.tranthaistore.model.User;
import com.tranthai.tranthaistore.service.BrandService;
import com.tranthai.tranthaistore.service.CategoryService;
import com.tranthai.tranthaistore.service.ProductService;
import com.tranthai.tranthaistore.service.UserService;
import com.tranthai.tranthaistore.utils.CartUtil;
import com.tranthai.tranthaistore.utils.UserUtil;

@Controller
public class ShopController {

    @Autowired
    private UserUtil userHelper;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private BrandService brandService;

    @Autowired
    private CartUtil cartUtil;

    @ModelAttribute("email")
    public String getCurrentEmail(HttpSession session) {
        String email = userHelper.getCurrentUsername();
        if (!"anonymousUser".equals(email)) {
            session.setAttribute("email", email);
        }
        return email;
    }


    @ModelAttribute("userId")
    public Long getCurrentUserId() {
        String email = userHelper.getCurrentUsername();
        User user = userService.getUserByEmail(email);
        return user != null ? user.getId() : null;
    }

    @GetMapping({ "/", "/home" })
    public String home(Model model, HttpSession session) {
        model.addAttribute("products", this.productService.getAllProductSort());
        model.addAttribute("categories", this.categoryService.getAllCategory());
        model.addAttribute("brands", this.brandService.getAllBrand());
        Map<Long, Integer> cart = (Map<Long, Integer>) session.getAttribute("cart");
        this.cartUtil.handleCartUpdate(session, cart);
        return "index";
    }

    @GetMapping("/shop")
    public String shop(@RequestParam(value = "page", required = false, defaultValue = "1") Integer page, 
        @RequestParam(value = "keyword", required = false) String keyword, Model model, HttpSession session) {

        if (page <= 0) {
            page = 1;
        }

        List<Product> products = (List<Product>) model.getAttribute("products");
        String titlePage = (String) model.getAttribute("titlePage");
        Long categoryId = (Long) model.getAttribute("categoryId");
        Long brandId = (Long) model.getAttribute("brandId");
        Pageable pageable = PageRequest.of(page - 1, 20);
        Page<Product> productPage;
        
        if (keyword != null && products != null) {
            productPage = this.productService.searchProductPage(keyword, pageable);
        } else if (categoryId != null && products != null) {
            productPage = this.productService.getAllProductByCategoryPage(categoryId, pageable);
        } else if (brandId != null && products !=null) {
            productPage = this.productService.getAllProductByBrandPage(brandId, pageable);
        } else {
            productPage = this.productService.getAllProductPage(pageable);
        }
        model.addAttribute("products", productPage.getContent());
        model.addAttribute("totalPages", productPage.getTotalPages());
        
        model.addAttribute("currentPage", page);
        model.addAttribute("categories", this.categoryService.getAllCategory());
        model.addAttribute("brands", this.brandService.getAllBrand());
        model.addAttribute("titlePage", titlePage);
        // Lấy giỏ hàng từ session, nếu không có thì khởi tạo
        Map<Long, Integer> cart = (Map<Long, Integer>) session.getAttribute("cart");
        this.cartUtil.handleCartUpdate(session, cart);
        return "shop";
    }

    @GetMapping("/shop/category/{id}")
    public String shopByCategory(RedirectAttributes redirectAttributes, @PathVariable Long id, HttpSession session) {
        List<Product> products = this.productService.getAllProductByCategory(id);
        String categoryName = this.categoryService.getCategoryById(id).get().getName();
        redirectAttributes.addFlashAttribute("products", products);
        redirectAttributes.addFlashAttribute("categoryId", id);
        redirectAttributes.addFlashAttribute("titlePage", categoryName);
        
        return "redirect:/shop";
    }

    @GetMapping("/shop/brand/{id}")
    public String shopByBrand(RedirectAttributes redirectAttributes, @PathVariable Long id){
        List<Product> products = this.productService.getAllProductByBrand(id);
        String brandName = this.brandService.getBrandById(id).get().getName();
        redirectAttributes.addFlashAttribute("products", products);
        redirectAttributes.addFlashAttribute("brandId", id);
        redirectAttributes.addFlashAttribute("titlePage", brandName);

        return "redirect:/shop";
    }

    @GetMapping("/shop/search")
    public String search(@RequestParam String keyword, RedirectAttributes redirectAttributes) {
        keyword = keyword.trim();
        List<Product> results = this.productService.searchProduct(keyword);
        redirectAttributes.addFlashAttribute("products", results);
        redirectAttributes.addAttribute("keyword", keyword);
        return "redirect:/shop";
    }

    @GetMapping("/shop/viewproduct/{id}")
    public String viewProductDetail(Model model, @PathVariable int id, HttpSession session) {
        model.addAttribute("product", this.productService.getProductById(id).get());

        return "viewProduct";

    }

}
