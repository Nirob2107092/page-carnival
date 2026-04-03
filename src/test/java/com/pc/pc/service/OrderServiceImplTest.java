package com.pc.pc.service;

import com.pc.pc.dto.CartDto;
import com.pc.pc.dto.CartItemDto;
import com.pc.pc.dto.OrderHistoryDto;
import com.pc.pc.dto.OrderUpdateStatusDto;
import com.pc.pc.exception.EmptyCartException;
import com.pc.pc.exception.InsufficientStockException;
import com.pc.pc.exception.ResourceNotFoundException;
import com.pc.pc.model.*;
import com.pc.pc.repository.BookRepository;
import com.pc.pc.repository.OrderRepository;
import com.pc.pc.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CartService cartService;

    @InjectMocks
    private OrderServiceImpl orderService;

    private User buyer;

    @BeforeEach
    void setUp() {
        buyer = new User();
        ReflectionTestUtils.setField(buyer, "id", 1L);
    }

    @Test
    void placeOrderShouldThrowWhenCartIsEmpty() {
        CartDto cart = new CartDto();
        cart.setItems(List.of());

        when(userRepository.findById(1L)).thenReturn(Optional.of(buyer));
        when(cartService.getCart()).thenReturn(cart);

        assertThrows(EmptyCartException.class, () -> orderService.placeOrder(1L));
    }

    @Test
    void placeOrderShouldThrowWhenStockIsInsufficient() {
        CartItemDto item = new CartItemDto();
        item.setBookId(10L);
        item.setQuantity(5);
        item.setPrice(new BigDecimal("10.00"));
        item.setSubtotal(new BigDecimal("50.00"));

        CartDto cart = new CartDto();
        cart.setItems(List.of(item));
        cart.setTotal(new BigDecimal("50.00"));

        Book book = new Book();
        book.setTitle("Low Stock Book");
        book.setStock(2);

        when(userRepository.findById(1L)).thenReturn(Optional.of(buyer));
        when(cartService.getCart()).thenReturn(cart);
        when(bookRepository.findById(10L)).thenReturn(Optional.of(book));

        assertThrows(InsufficientStockException.class, () -> orderService.placeOrder(1L));
    }

    @Test
    void placeOrderShouldSaveOrderAndClearCart() {
        CartItemDto item = new CartItemDto();
        item.setBookId(10L);
        item.setQuantity(2);
        item.setPrice(new BigDecimal("15.00"));
        item.setSubtotal(new BigDecimal("30.00"));

        CartDto cart = new CartDto();
        cart.setItems(List.of(item));
        cart.setTotal(new BigDecimal("30.00"));

        Book book = new Book();
        book.setTitle("In Stock Book");
        book.setStock(5);

        when(userRepository.findById(1L)).thenReturn(Optional.of(buyer));
        when(cartService.getCart()).thenReturn(cart);
        when(bookRepository.findById(10L)).thenReturn(Optional.of(book));

        orderService.placeOrder(1L);

        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository).save(orderCaptor.capture());
        verify(cartService).clearCart();
        assertEquals(OrderStatus.PENDING, orderCaptor.getValue().getStatus());
        assertEquals(new BigDecimal("30.00"), orderCaptor.getValue().getTotalPrice());
    }

    @Test
    void getOrderByIdShouldThrowWhenOrderNotOwnedByBuyer() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(buyer));
        when(orderRepository.findByIdAndBuyer(100L, buyer)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> orderService.getOrderById(1L, 100L));
    }

    @Test
    void updateOrderStatusShouldPersistAndReturnUpdatedDto() {
        Order order = new Order();
        ReflectionTestUtils.setField(order, "id", 20L);
        order.setBuyer(buyer);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);
        order.setTotalPrice(new BigDecimal("80.00"));
        order.setOrderItems(List.of());

        OrderUpdateStatusDto request = new OrderUpdateStatusDto();
        request.setStatus(OrderStatus.CONFIRMED);

        when(userRepository.findById(1L)).thenReturn(Optional.of(buyer));
        when(orderRepository.findByIdAndBuyer(20L, buyer)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        OrderHistoryDto result = orderService.updateOrderStatus(1L, 20L, request);

        assertEquals("CONFIRMED", result.getStatus());
        verify(orderRepository).save(order);
    }
}
