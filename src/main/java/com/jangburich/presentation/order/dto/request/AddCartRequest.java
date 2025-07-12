package com.jangburich.presentation.order.dto.request;

public record AddCartRequest(
        Long storeId,
        Long menuId,
        int quantity
) {
}