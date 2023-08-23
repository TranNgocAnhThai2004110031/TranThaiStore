package com.tranthai.tranthaistore.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.tranthai.tranthaistore.utils.UserUtil;

@Controller
public class AuthController {
    @Autowired
    private UserUtil userUtil;

    @GetMapping("/login")
    public String login(HttpSession session) {
        session.setAttribute("loggedIn", true);
        session.setAttribute("email", userUtil.getCurrentUsername());
        return "login";
    }

    @GetMapping("/403")
    public String errorPage() {
        return "403";
    }
}
