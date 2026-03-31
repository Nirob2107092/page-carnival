package com.pc.pc.service;

import com.pc.pc.dto.CartDto;
import com.pc.pc.exception.ResourceNotFoundException;
import com.pc.pc.model.Book;
import com.pc.pc.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CartServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private CartServiceImpl cartService;

    private Book book;

    @BeforeEach
    void setUp() {
        book = new Book();
        ReflectionTestUtils.setField(book, "id", 1L);
        book.setTitle("Spring in Action");
        book.setPrice(new BigDecimal("30.00"));
    }

    @Test
    void addToCartShouldAddNewItem() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        cartService.addToCart(1L, 2);
        CartDto cart = cartService.getCart();

        assertEquals(1, cart.getItems().size());
        assertEquals(new BigDecimal("60.00"), cart.getTotal());
    }

    @Test
    void addToCartShouldIncreaseQuantityForExistingItem() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        cartService.addToCart(1L, 1);
        cartService.addToCart(1L, 2);

        CartDto cart = cartService.getCart();
        assertEquals(1, cart.getItems().size());
        assertEquals(3, cart.getItems().get(0).getQuantity());
        assertEquals(new BigDecimal("90.00"), cart.getTotal());
    }

    @Test
    void addToCartShouldThrowWhenBookNotFound() {
        when(bookRepository.findById(77L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> cartService.addToCart(77L, 1));
    }

    @Test
    void updateQuantityShouldRecalculateSubtotal() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        cartService.addToCart(1L, 1);
        cartService.updateQuantity(1L, 4);

        CartDto cart = cartService.getCart();
        assertEquals(4, cart.getItems().get(0).getQuantity());
        assertEquals(new BigDecimal("120.00"), cart.getTotal());
    }

    @Test
    void clearCartShouldRemoveAllItemsAndResetTotal() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        cartService.addToCart(1L, 1);
        cartService.clearCart();

        CartDto cart = cartService.getCart();
        assertTrue(cart.getItems().isEmpty());
        assertEquals(BigDecimal.ZERO, cart.getTotal());
    }
}
