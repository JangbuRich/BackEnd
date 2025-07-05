package com.jangburich.domain.user.dto.response;

import com.querydsl.core.annotations.QueryProjection;

import java.time.LocalDateTime;

public record TeamsResponse(
        Long teamId,
        Long storeId,
        int dDay,
        String storeImgUrl,
        Boolean isLikedAtStore,
        String teamName,
        String storeName,
        int totalAmount,
        int currentAmount,
        LocalDateTime createdAt
) {

    @QueryProjection
    public TeamsResponse(Long teamId, Long storeId, int dDay, String storeImgUrl, Boolean isLikedAtStore,
                         String teamName,
                         String storeName, int totalAmount, int currentAmount
    , LocalDateTime createdAt) {
        this.teamId = teamId;
        this.storeId = storeId;
        this.dDay = dDay;
        this.storeImgUrl = storeImgUrl;
        this.isLikedAtStore = isLikedAtStore;
        this.teamName = teamName;
        this.storeName = storeName;
        this.totalAmount = totalAmount;
        this.currentAmount = currentAmount;
        this.createdAt = createdAt;
    }
}
