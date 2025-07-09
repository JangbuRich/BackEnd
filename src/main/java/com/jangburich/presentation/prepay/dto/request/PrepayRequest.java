package com.jangburich.presentation.prepay.dto.request;

public record PrepayRequest(
        Long storeId,
        Long teamId,
        int prepayAmount,
        int personalAllocatedAmount
) {
}
