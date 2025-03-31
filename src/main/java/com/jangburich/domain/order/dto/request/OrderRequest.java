package com.jangburich.domain.order.dto.request;

public record OrderRequest(
        Long storeId,
        Long teamId,
        Integer price
) {
}
