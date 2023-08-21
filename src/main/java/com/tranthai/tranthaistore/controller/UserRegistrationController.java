package com.tranthai.tranthaistore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tranthai.tranthaistore.dto.UserDTO;
import com.tranthai.tranthaistore.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;



@Controller
@RequestMapping("/register")
public class UserRegistrationController {
    @Autowired
    private UserService userService;

    public UserRegistrationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserDTO());
        return "register";
    }

    @PostMapping()
    public String registerUserAccount(@ModelAttribute("user") UserDTO userDTO, Model model) {
        try {
            if (this.userService.loadUserByUsername(userDTO.getEmail()) != null) {
                model.addAttribute("success", false);
                return "redirect:/register?error";
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        model.addAttribute("success", true);
        userService.addUser(userDTO);
        return "redirect:/register?success";
    }
    
    
    
}
