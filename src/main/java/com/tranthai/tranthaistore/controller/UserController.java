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

import com.tranthai.tranthaistore.converter.UserConverter;
import com.tranthai.tranthaistore.dto.UserDTO;
import com.tranthai.tranthaistore.model.User;
import com.tranthai.tranthaistore.service.UserService;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserConverter userConverter;

    @GetMapping("/admin/user/add")
    public String getUser(Model model){
        model.addAttribute("userDTO", new UserDTO());
        model.addAttribute("titlePage", "ADD USER, ADMIN");

        return "userAdd";
    }

    @PostMapping("/admin/user/add")
    public String addUser(@ModelAttribute("userDTO") UserDTO userDTO) {
        User user = this.userConverter.toEntity(userDTO);
        this.userService.updateUser(user);
        return "redirect:/admin/users";
    }

    @GetMapping("/admin/user/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id){
        this.userService.removeUserById(id);
        return "redirect:/admin/users";
    }

    @GetMapping("/admin/user/{id}")
    public String updateUser(@PathVariable("id") Long id, Model model) {
        UserDTO userDTO = this.userConverter.toDTO(this.userService.getUserById(id).get());
        model.addAttribute("userDTO", userDTO);
        model.addAttribute("isUpdate", true);
        model.addAttribute("titlePage", "UPDATE USER, ADMIN");

        return "userAdd";
    }

    @GetMapping("/admin/users/search")
    public String searchUser(@RequestParam("keyword") String keyword, RedirectAttributes redirectAttributes){
        List<User> users = this.userService.searchUser(keyword);
        redirectAttributes.addFlashAttribute("users", users);
        return "redirect:/admin/users";
    }
}
