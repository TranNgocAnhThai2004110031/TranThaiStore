package com.tranthai.tranthaistore.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.tranthai.tranthaistore.model.Product;
import com.tranthai.tranthaistore.model.User;
import com.tranthai.tranthaistore.service.CategoryService;
import com.tranthai.tranthaistore.service.ProductService;
import com.tranthai.tranthaistore.service.UserService;
import com.tranthai.tranthaistore.util.UserHelper;

@Controller
public class ShopController {

    @Autowired
    private UserHelper userHelper;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

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
        // String email = userHelper.getCurrentUsername();
        // if (email != null) {
        //     User user = userService.getUserByEmail(email);
        //     if (user != null) {
        //         return user.getId();
        //     }
        //     return null;
        // }
        // return null;
        String email = userHelper.getCurrentUsername();
        User user = userService.getUserByEmail(email);
        return user != null ? user.getId() : null;
    }

    // @GetMapping({ "/", "/home" })
    // public String home(Model model, HttpSession session){
    //     session.setAttribute("session", session);
    //     String email =  this.userHelper.getCurrentUsername();
    //     session.setAttribute("email", email);
    //     User user = this.userService.getUserByEmail(email);
    //     session.setAttribute("userid", user.getId());
    //     model.addAttribute("products", this.productService.getAllProduct());
    //     model.addAttribute("categories", categoryService.getAllCategory());
    //     return "home";
    // }

    // @GetMapping("/shop")
    // public String shop(Model model, HttpSession session, Authentication authentication) {
    //     session.setAttribute("session", session);
    //     String email =  this.userHelper.getCurrentUsername();
    //     session.setAttribute("email", email);
    //     User user = this.userService.getUserByEmail(email);
    //     session.setAttribute("userid", user.getId());
    //     model.addAttribute("categories", categoryService.getAllCategory());
    //     model.addAttribute("products", productService.getAllProduct());
    //     return "shop";
    // }
    @GetMapping({ "/", "/home" })
    public String home(Model model, HttpSession session) {
        model.addAttribute("products", this.productService.getAllProduct());
        model.addAttribute("categories", this.categoryService.getAllCategory());
        return "home";
    }

    @GetMapping("/shop")
    public String shop(Model model, HttpSession session) {
        model.addAttribute("categories", this.categoryService.getAllCategory());
        model.addAttribute("products", this.productService.getAllProduct());
        return "shop";
    }

    @GetMapping("/shop/category/{id}")
    public String shopByCategory(Model model, @PathVariable int id, HttpSession session) {
        model.addAttribute("categories", this.categoryService.getAllCategory());
        model.addAttribute("products", this.productService.getAllProductByCategory(id));
        return "shop";
    }

    @GetMapping("/shop/search")
    public String search(@RequestParam String keyword, Model model) {
        keyword = keyword.trim();
        List<Product> results = this.productService.searchProduct(keyword);
        model.addAttribute("categories", this.categoryService.getAllCategory());
        model.addAttribute("products", results);

        return "shop";
    }

    @GetMapping("/shop/viewproduct/{id}")
    public String viewProductDetail(Model model, @PathVariable int id, HttpSession session) {
        model.addAttribute("product", this.productService.getProductById(id).get());
        // session.setAttribute("session", session);

        return "viewProduct";

    }
}
