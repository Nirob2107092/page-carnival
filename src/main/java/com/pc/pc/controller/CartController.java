package com.pc.pc.controller;

import com.pc.pc.service.CartService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
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

    @PostMapping("/update")
    public String updateCart(@RequestParam Long bookId,
                             @RequestParam Integer quantity) {
        cartService.updateQuantity(bookId, quantity);
        return "redirect:/buyer/cart";
    }

    @GetMapping("/remove/{bookId}")
    public String removeFromCart(@PathVariable Long bookId) {
        cartService.removeFromCart(bookId);
        return "redirect:/buyer/cart";
    }

    @GetMapping("/clear")
    public String clearCart() {
        cartService.clearCart();
        return "redirect:/buyer/cart";
    }
}