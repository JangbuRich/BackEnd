package com.jangburich.presentation.wallet.dto.response;

import java.time.LocalDateTime;

public record AvailableOrder(
        String dateTime,
        Integer amount,
        String storeName,
        String storeCategory
) {
}