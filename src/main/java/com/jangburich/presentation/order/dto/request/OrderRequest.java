package com.jangburich.presentation.order.dto.request;

public record OrderRequest(
        Long storeId,
        Long teamId,
        Integer quantity
) {
}
