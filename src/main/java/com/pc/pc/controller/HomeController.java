package com.pc.pc.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.pc.pc.exception.ResourceNotFoundException;
import com.pc.pc.model.RoleType;
import com.pc.pc.model.User;
import com.pc.pc.repository.UserRepository;
import com.pc.pc.service.BookService;

@Controller
public class HomeController {

    private final BookService bookService;
    private final UserRepository userRepository;

    public HomeController(BookService bookService, UserRepository userRepository) {
        this.bookService = bookService;
        this.userRepository = userRepository;
    }

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/dashboard")
    public String adminDashboard(Model model) {
        model.addAttribute("buyerCount", userRepository.countByRole_Name(RoleType.BUYER));
        model.addAttribute("sellerCount", userRepository.countByRole_Name(RoleType.SELLER));
        model.addAttribute("sellers", userRepository.findByRole_Name(RoleType.SELLER));
        return "admin-dashboard";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/catalog")
    public String adminBookCatalog(Model model) {
        model.addAttribute("books", bookService.getAllBooks());
        return "admin-book-catalog";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/sellers/{id}/toggle")
    public String toggleSeller(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        User seller = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));
        seller.setEnabled(!seller.getEnabled());
        userRepository.save(seller);
        redirectAttributes.addFlashAttribute("message",
                "Seller " + (seller.getEnabled() ? "enabled" : "disabled") + " successfully.");
        return "redirect:/admin/dashboard";
    }

    @PreAuthorize("hasRole('SELLER')")
    @GetMapping("/seller/dashboard")
    public String sellerDashboard() {
        return "seller-dashboard";
    }

    @PreAuthorize("hasRole('BUYER')")
    @GetMapping("/buyer/dashboard")
    public String buyerDashboard() {
        return "buyer-dashboard";
    }

    @PreAuthorize("hasRole('BUYER')")
    @GetMapping("/buyer/catalog")
    public String bookCatalog(Model model) {
        model.addAttribute("books", bookService.getBuyerVisibleBooks());
        return "book-catalog";
    }
}
