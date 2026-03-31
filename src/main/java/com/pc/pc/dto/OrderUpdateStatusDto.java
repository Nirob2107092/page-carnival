package com.pc.pc.dto;

import com.pc.pc.model.OrderStatus;
import jakarta.validation.constraints.NotNull;

public class OrderUpdateStatusDto {

    @NotNull(message = "status is required")
    private OrderStatus status;

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}
