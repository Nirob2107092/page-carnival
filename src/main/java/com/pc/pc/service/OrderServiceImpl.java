package com.pc.pc.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.pc.pc.dto.CartDto;
import com.pc.pc.dto.OrderHistoryDto;
import com.pc.pc.dto.OrderItemDto;
import com.pc.pc.dto.OrderUpdateStatusDto;
import com.pc.pc.exception.EmptyCartException;
import com.pc.pc.exception.InsufficientStockException;
import com.pc.pc.exception.ResourceNotFoundException;
import com.pc.pc.model.Book;
import com.pc.pc.model.Order;
import com.pc.pc.model.OrderItem;
import com.pc.pc.model.OrderStatus;
import com.pc.pc.model.User;
import com.pc.pc.repository.BookRepository;
import com.pc.pc.repository.OrderRepository;
import com.pc.pc.repository.UserRepository;

@Service
public class OrderServiceImpl implements OrderService {

    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final OrderRepository orderRepository;
    private final CartService cartService;

    public OrderServiceImpl(UserRepository userRepository,
                            BookRepository bookRepository,
                            OrderRepository orderRepository,
                            CartService cartService) {
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
        this.orderRepository = orderRepository;
        this.cartService = cartService;
    }

    @Override
    public void placeOrder(Long buyerId) {
        User buyer = getBuyerById(buyerId);
        CartDto cart = cartService.getCart();

        if (cart.getItems().isEmpty()) {
            throw new EmptyCartException();
        }

        Order order = new Order();
        order.setBuyer(buyer);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);
        order.setTotalPrice(cart.getTotal());

        List<OrderItem> orderItems = cart.getItems().stream().map(cartItem -> {
            Book book = bookRepository.findById(cartItem.getBookId())
                    .orElseThrow(() -> new ResourceNotFoundException("Book", cartItem.getBookId()));

            if (book.getStock() < cartItem.getQuantity()) {
                throw new InsufficientStockException(book.getTitle());
            }

            book.setStock(book.getStock() - cartItem.getQuantity());
            bookRepository.save(book);

            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setBook(book);
            item.setQuantity(cartItem.getQuantity());
            item.setUnitPrice(cartItem.getPrice());
            item.setSubtotal(cartItem.getSubtotal());
            return item;
        }).collect(Collectors.toList());

        order.setOrderItems(orderItems);
        orderRepository.save(order);
        cartService.clearCart();
    }

    @Override
    public List<OrderHistoryDto> getOrdersByBuyer(Long buyerId) {
        User buyer = getBuyerById(buyerId);
        return orderRepository.findByBuyer(buyer).stream()
                .map(this::mapToHistoryDto)
                .collect(Collectors.toList());
    }

    @Override
    public OrderHistoryDto getOrderById(Long buyerId, Long orderId) {
        User buyer = getBuyerById(buyerId);
        Order order = orderRepository.findByIdAndBuyer(orderId, buyer)
                .orElseThrow(() -> new ResourceNotFoundException("Order", orderId));
        return mapToHistoryDto(order);
    }

    @Override
    public OrderHistoryDto updateOrderStatus(Long buyerId, Long orderId, OrderUpdateStatusDto request) {
        User buyer = getBuyerById(buyerId);
        Order order = orderRepository.findByIdAndBuyer(orderId, buyer)
                .orElseThrow(() -> new ResourceNotFoundException("Order", orderId));
        order.setStatus(request.getStatus());
        return mapToHistoryDto(orderRepository.save(order));
    }

    @Override
    public void deleteOrder(Long buyerId, Long orderId) {
        User buyer = getBuyerById(buyerId);
        Order order = orderRepository.findByIdAndBuyer(orderId, buyer)
                .orElseThrow(() -> new ResourceNotFoundException("Order", orderId));
        orderRepository.delete(order);
    }

    private OrderHistoryDto mapToHistoryDto(Order order) {
        OrderHistoryDto dto = new OrderHistoryDto();
        dto.setOrderId(order.getId());
        dto.setOrderDate(order.getOrderDate());
        dto.setStatus(order.getStatus().name());
        dto.setTotalPrice(order.getTotalPrice());

        List<OrderItemDto> items = order.getOrderItems().stream().map(item -> {
            OrderItemDto itemDto = new OrderItemDto();
            itemDto.setBookTitle(item.getBook().getTitle());
            itemDto.setQuantity(item.getQuantity());
            itemDto.setUnitPrice(item.getUnitPrice());
            itemDto.setSubtotal(item.getSubtotal());
            return itemDto;
        }).collect(Collectors.toList());

        dto.setItems(items);
        return dto;
    }

    private User getBuyerById(Long buyerId) {
        return userRepository.findById(buyerId)
                .orElseThrow(() -> new ResourceNotFoundException("User", buyerId));
    }
}
