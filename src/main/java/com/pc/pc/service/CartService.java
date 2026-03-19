package com.pc.pc.service;

import com.pc.pc.dto.CartDto;

public interface CartService {
    CartDto getCart();
    void addToCart(Long bookId, Integer quantity);
    void removeFromCart(Long bookId);
    void updateQuantity(Long bookId, Integer quantity);
    void clearCart();
}