package com.jangburich.presentation.order.dto.response;

import com.querydsl.core.annotations.QueryProjection;

public record OrderItemResponse(
        String menuName,
        int quantity,
        int price
) {

    @QueryProjection
    public OrderItemResponse(String menuName, int quantity, int price) {
        this.menuName = menuName;
        this.quantity = quantity;
        this.price = price;
    }
}
