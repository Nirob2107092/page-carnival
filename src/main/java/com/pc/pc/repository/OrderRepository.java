package com.pc.pc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pc.pc.model.Order;
import com.pc.pc.model.User;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByBuyer(User buyer);
}