package com.pc.pc.service;

import java.util.List;

import com.pc.pc.dto.OrderHistoryDto;
import com.pc.pc.dto.OrderUpdateStatusDto;
import com.pc.pc.dto.SellerOrderItemDto;
import com.pc.pc.model.OrderStatus;

public interface OrderService {
    void placeOrder(Long buyerId);
    List<OrderHistoryDto> getOrdersByBuyer(Long buyerId);
    OrderHistoryDto getOrderById(Long buyerId, Long orderId);
    OrderHistoryDto updateOrderStatus(Long buyerId, Long orderId, OrderUpdateStatusDto request);
    void deleteOrder(Long buyerId, Long orderId);
    List<SellerOrderItemDto> getOrderItemsForSeller(Long sellerId);
    void updateOrderStatusBySeller(Long orderId, Long sellerId, OrderStatus newStatus);
}
