package com.tranthai.tranthaistore.controller;

import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ShopController {
    @GetMapping({ "/", "/home" })
    public String home(Model model, HttpSession session, Authentication authentication){
        session.setAttribute("session", session);
        
        return "home";
    }
}
