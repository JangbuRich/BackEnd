package com.jangburich.presentation.wallet.dto.response;

public record AvailableOrder(
        String date,
        Integer amount,
        String transactionTitle,
        String transactionType
) {
}
