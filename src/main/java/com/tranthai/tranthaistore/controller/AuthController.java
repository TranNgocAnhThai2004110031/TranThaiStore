package com.tranthai.tranthaistore.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {
    @GetMapping("/login")
    public String login(HttpSession session) {
        session.setAttribute("loggedIn", true);
        return "login";
    }

    @GetMapping("/403")
    public String errorPage() {
        return "403";
    }
}
