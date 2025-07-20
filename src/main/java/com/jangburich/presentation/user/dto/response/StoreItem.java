package com.jangburich.presentation.user.dto.response;

public record StoreItem (
        Long storeId
        , String storeName
        , Integer remainingBalance
        , Integer totalBalance
        , String storeImageUrl
        , boolean isLiked
        , String groupName
) {
}
