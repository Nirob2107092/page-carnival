package com.pc.pc.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pc.pc.model.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}