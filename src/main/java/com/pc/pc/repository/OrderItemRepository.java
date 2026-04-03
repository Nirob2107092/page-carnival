package com.pc.pc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pc.pc.model.OrderItem;
import com.pc.pc.model.User;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findByBook_Seller(User seller);
}
