package com.jangburich.presentation.wallet.dto.response;

public record PurchaseHistory(
        String date,
        Integer amount,
        String transactionTitle,
        String transactionType
) {
}
