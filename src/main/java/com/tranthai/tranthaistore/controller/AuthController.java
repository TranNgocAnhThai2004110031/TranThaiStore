package com.tranthai.tranthaistore.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.tranthai.tranthaistore.service.BrandService;
import com.tranthai.tranthaistore.service.CategoryService;
import com.tranthai.tranthaistore.utils.UserUtil;

@Controller
public class AuthController {
    @Autowired
    private UserUtil userUtil;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private BrandService brandService;

    @GetMapping("/login")
    public String login(Model model,HttpSession session) {
        session.setAttribute("loggedIn", true);
        session.setAttribute("email", userUtil.getCurrentUsername());
        model.addAttribute("categories", this.categoryService.getAllCategory());
        model.addAttribute("brands", this.brandService.getAllBrand());
        return "login";
    }

    @GetMapping("/403")
    public String errorPage() {
        return "403";
    }
}
