package com.pc.pc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.pc.pc.service.BookService;

@Controller
public class HomeController {

    private final BookService bookService;

    public HomeController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/admin/dashboard")
    public String adminDashboard() {
        return "admin-dashboard";
    }

    @GetMapping("/seller/dashboard")
    public String sellerDashboard() {
        return "seller-dashboard";
    }

    @GetMapping("/buyer/dashboard")
    public String buyerDashboard() {
        return "buyer-dashboard";
    }

    @GetMapping("/buyer/catalog")
    public String bookCatalog(Model model) {
        model.addAttribute("books", bookService.getAllBooks());
        return "book-catalog";
    }
}