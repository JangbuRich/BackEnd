package com.jangburich.presentation.team.dto.response;

import java.time.LocalDate;

public record TeamPrepaidStoreItem(Long storeId, String storeName, LocalDate expirationDate, String imageUrl, Integer totalBalance, Integer remainingBalance, Boolean isLiked) {
    public TeamPrepaidStoreItem(Long storeId, String storeName, LocalDate expirationDate, String imageUrl, Integer totalBalance,Integer remainingBalance, Boolean isLiked){
        this.storeId = storeId;
        this.storeName = storeName;
        this.expirationDate = expirationDate;
        this.imageUrl = imageUrl;
        this.totalBalance = totalBalance;
        this.remainingBalance = remainingBalance;
        this.isLiked = isLiked;
    }
}
