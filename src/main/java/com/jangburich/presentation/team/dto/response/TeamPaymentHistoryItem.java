package com.jangburich.presentation.team.dto.response;

import java.time.LocalDateTime;

public record TeamPaymentHistoryItem(LocalDateTime transactionDateTime, String storeName, Integer amount) {
}
