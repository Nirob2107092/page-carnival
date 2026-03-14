package com.pc.pc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.pc.pc.dto.UserRegistrationDto;
import com.pc.pc.model.RoleType;
import com.pc.pc.service.AuthService;

@Controller
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserRegistrationDto());
        model.addAttribute("roles", RoleType.values());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") UserRegistrationDto registrationDto) {
        authService.registerUser(registrationDto);
        return "redirect:/login?registered";
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }
}