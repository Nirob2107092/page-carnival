package com.pc.pc.controller;

import com.pc.pc.dto.CartDto;
import com.pc.pc.service.CartService;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@PreAuthorize("hasRole('BUYER')")
public class ApiCartController {

    private final CartService cartService;

    public ApiCartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public ResponseEntity<CartDto> getCart() {
        return ResponseEntity.ok(cartService.getCart());
    }

    @PostMapping("/items")
    public ResponseEntity<CartDto> addToCart(@RequestParam @NotNull Long bookId,
                                             @RequestParam(defaultValue = "1") @Min(1) Integer quantity) {
        cartService.addToCart(bookId, quantity);
        return ResponseEntity.ok(cartService.getCart());
    }

    @PatchMapping("/items/{bookId}")
    public ResponseEntity<CartDto> updateCartItem(@PathVariable Long bookId,
                                                  @RequestParam @Min(0) Integer quantity) {
        cartService.updateQuantity(bookId, quantity);
        return ResponseEntity.ok(cartService.getCart());
    }

    @DeleteMapping("/items/{bookId}")
    public ResponseEntity<Void> removeCartItem(@PathVariable Long bookId) {
        cartService.removeFromCart(bookId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> clearCart() {
        cartService.clearCart();
        return ResponseEntity.noContent().build();
    }
}
