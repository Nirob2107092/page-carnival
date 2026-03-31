package com.pc.pc.controller;

import com.pc.pc.dto.OrderHistoryDto;
import com.pc.pc.dto.OrderUpdateStatusDto;
import com.pc.pc.security.CustomUserDetails;
import com.pc.pc.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@PreAuthorize("hasRole('BUYER')")
public class ApiOrderController {

    private final OrderService orderService;

    public ApiOrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Void> placeOrder(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long buyerId = userDetails.getUser().getId();
        orderService.placeOrder(buyerId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<OrderHistoryDto>> getOrderHistory(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long buyerId = userDetails.getUser().getId();
        return ResponseEntity.ok(orderService.getOrdersByBuyer(buyerId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderHistoryDto> getOrderById(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                        @PathVariable("id") Long orderId) {
        Long buyerId = userDetails.getUser().getId();
        return ResponseEntity.ok(orderService.getOrderById(buyerId, orderId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderHistoryDto> updateOrder(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                       @PathVariable("id") Long orderId,
                                                       @Valid @RequestBody OrderUpdateStatusDto request) {
        Long buyerId = userDetails.getUser().getId();
        return ResponseEntity.ok(orderService.updateOrderStatus(buyerId, orderId, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@AuthenticationPrincipal CustomUserDetails userDetails,
                                            @PathVariable("id") Long orderId) {
        Long buyerId = userDetails.getUser().getId();
        orderService.deleteOrder(buyerId, orderId);
        return ResponseEntity.noContent().build();
    }
}
