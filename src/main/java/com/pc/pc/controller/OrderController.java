package com.pc.pc.controller;

import com.pc.pc.security.CustomUserDetails;
import com.pc.pc.service.OrderService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@PreAuthorize("hasRole('BUYER')")
@RequestMapping("/buyer/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/place")
    public String placeOrder(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long buyerId = userDetails.getUser().getId();
        orderService.placeOrder(buyerId);
        return "redirect:/buyer/orders/history";
    }

    @GetMapping("/history")
    public String viewOrderHistory(@AuthenticationPrincipal CustomUserDetails userDetails,
                                   Model model) {
        Long buyerId = userDetails.getUser().getId();
        model.addAttribute("orders", orderService.getOrdersByBuyer(buyerId));
        return "order-history";
    }
}