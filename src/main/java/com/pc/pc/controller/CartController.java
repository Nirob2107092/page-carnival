package com.pc.pc.controller;

import com.pc.pc.service.CartService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@PreAuthorize("hasRole('BUYER')")
@RequestMapping("/buyer/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public String viewCart(Model model) {
        model.addAttribute("cart", cartService.getCart());
        return "cart";
    }

    @PostMapping("/add")
    public String addToCart(@RequestParam Long bookId,
                            @RequestParam(defaultValue = "1") Integer quantity) {
        cartService.addToCart(bookId, quantity);
        return "redirect:/buyer/cart";
    }

    @PatchMapping("/update")
    public String updateCart(@RequestParam Long bookId,
                             @RequestParam Integer quantity) {
        cartService.updateQuantity(bookId, quantity);
        return "redirect:/buyer/cart";
    }

    @DeleteMapping("/remove/{bookId}")
    public String removeFromCart(@PathVariable Long bookId) {
        cartService.removeFromCart(bookId);
        return "redirect:/buyer/cart";
    }

    @DeleteMapping("/clear")
    public String clearCart() {
        cartService.clearCart();
        return "redirect:/buyer/cart";
    }
}