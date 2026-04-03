package com.pc.pc.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.pc.pc.model.OrderStatus;
import com.pc.pc.security.CustomUserDetails;
import com.pc.pc.service.OrderService;

@Controller
@PreAuthorize("hasRole('SELLER')")
@RequestMapping("/seller/orders")
public class SellerOrderController {

    private final OrderService orderService;

    public SellerOrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public String viewOrders(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        model.addAttribute("orders", orderService.getOrderItemsForSeller(userDetails.getUser().getId()));
        return "seller-orders";
    }

    @PostMapping("/{orderId}/accept")
    public String acceptOrder(@PathVariable Long orderId,
                              @AuthenticationPrincipal CustomUserDetails userDetails) {
        orderService.updateOrderStatusBySeller(orderId, userDetails.getUser().getId(), OrderStatus.CONFIRMED);
        return "redirect:/seller/orders";
    }

    @PostMapping("/{orderId}/reject")
    public String rejectOrder(@PathVariable Long orderId,
                              @AuthenticationPrincipal CustomUserDetails userDetails) {
        orderService.updateOrderStatusBySeller(orderId, userDetails.getUser().getId(), OrderStatus.CANCELLED);
        return "redirect:/seller/orders";
    }
}
