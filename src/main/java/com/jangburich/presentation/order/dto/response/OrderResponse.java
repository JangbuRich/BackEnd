package com.jangburich.presentation.order.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import java.util.List;

public record OrderResponse(
        Long orderId,
        int totalAmount,
        List<OrderItemResponse> items
) {

    @QueryProjection
    public OrderResponse(Long orderId, int totalAmount, List<OrderItemResponse> items) {
        this.orderId = orderId;
        this.totalAmount = totalAmount;
        this.items = items;
    }
}
