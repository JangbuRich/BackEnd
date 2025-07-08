package com.jangburich.domain.store.presentation.dto.request;

public record PrepayRequest(
        Long storeId,
        Long teamId,
        int prepayAmount,
        int personalAllocatedAmount
) {
}
