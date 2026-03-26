package com.pc.pc.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.pc.pc.dto.CartDto;
import com.pc.pc.dto.CartItemDto;
import com.pc.pc.model.Book;
import com.pc.pc.repository.BookRepository;

@Service
public class CartServiceImpl implements CartService {

    private final BookRepository bookRepository;
    private final CartDto cart = new CartDto();

    public CartServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public CartDto getCart() {
        recalculateTotal();
        return cart;
    }

    @Override
    public void addToCart(Long bookId, Integer quantity) {
        Book book = bookRepository.findById(bookId).orElseThrow();

        CartItemDto existing = cart.getItems().stream()
                .filter(item -> item.getBookId().equals(bookId))
                .findFirst()
                .orElse(null);

        if (existing != null) {
            existing.setQuantity(existing.getQuantity() + quantity);
            existing.setSubtotal(existing.getPrice().multiply(BigDecimal.valueOf(existing.getQuantity())));
        } else {
            CartItemDto item = new CartItemDto();
            item.setBookId(book.getId());
            item.setTitle(book.getTitle());
            item.setPrice(book.getPrice());
            item.setQuantity(quantity);
            item.setSubtotal(book.getPrice().multiply(BigDecimal.valueOf(quantity)));
            cart.getItems().add(item);
        }

        recalculateTotal();
    }

    @Override
    public void removeFromCart(Long bookId) {
        cart.getItems().removeIf(item -> item.getBookId().equals(bookId));
        recalculateTotal();
    }

    @Override
    public void updateQuantity(Long bookId, Integer quantity) {
        for (CartItemDto item : cart.getItems()) {
            if (item.getBookId().equals(bookId)) {
                item.setQuantity(quantity);
                item.setSubtotal(item.getPrice().multiply(BigDecimal.valueOf(quantity)));
            }
        }
        recalculateTotal();
    }

    @Override
    public void clearCart() {
        cart.getItems().clear();
        cart.setTotal(BigDecimal.ZERO);
    }

    private void recalculateTotal() {
        BigDecimal total = cart.getItems().stream()
                .map(CartItemDto::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        cart.setTotal(total);
    }
}