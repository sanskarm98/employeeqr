package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import com.example.service.*;
import com.example.model.*;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String showLoginPage(Model model) {
        // Retrieve the list of users (optional, depending on your login page design)
        List<User> users = userService.getUsers(); 
        model.addAttribute("users", users);
        return "login";
    }

    @PostMapping("/login")
    public String handleLogin(String username, String password) {
        // Validate user credentials using the updated UserService
        if (userService.isValidUser(username, password)) {
            return "redirect:/home"; // Redirect to homepage on successful login
        } else {
            return "login"; // Return to login page on invalid credentials
        }
    }
}
