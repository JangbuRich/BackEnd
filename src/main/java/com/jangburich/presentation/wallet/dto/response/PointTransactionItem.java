package com.jangburich.presentation.wallet.dto.response;

import com.jangburich.domain.entity.Category;
import com.jangburich.domain.entity.OrderStatus;

import java.time.LocalDateTime;

public record PointTransactionItem(
        Long id
        , Long storeId
        , String storeName
        , Category storeCategory
        , Integer amount
        , OrderStatus status
        , LocalDateTime transactionDateTime
) {
}
