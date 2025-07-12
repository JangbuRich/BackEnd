package com.jangburich.presentation.order.dto.response;

import com.jangburich.domain.entity.OrderStatus;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;

import java.util.List;

public record OrderResponse(
        Long orderId,
        Long storeId,
        String storeName,
        int quantity,
        OrderStatus status
) {

    @QueryProjection
    public OrderResponse(Long orderId, Long storeId, String storeName, int quantity, OrderStatus status) {
        this.orderId = orderId;
        this.storeId=storeId;
        this.storeName=storeName;
        this.quantity=quantity;
        this.status=status;
    }
}
