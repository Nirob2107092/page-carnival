package com.pc.pc.service;

import java.util.List;

import com.pc.pc.dto.OrderHistoryDto;

public interface OrderService {
    void placeOrder(Long buyerId);
    List<OrderHistoryDto> getOrdersByBuyer(Long buyerId);
}