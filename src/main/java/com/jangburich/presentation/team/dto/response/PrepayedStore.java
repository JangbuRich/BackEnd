package com.jangburich.presentation.team.dto.response;

import com.querydsl.core.annotations.QueryProjection;

public record PrepayedStore(
        Long storeId,
        String storeName,
        String storeImgUrl,
        String address,
        Boolean isLiked
) {

    @QueryProjection
    public PrepayedStore(Long storeId, String storeName, String storeImgUrl, String address, Boolean isLiked) {
        this.storeId = storeId;
        this.storeName = storeName;
        this.storeImgUrl = storeImgUrl;
        this.address = address;
        this.isLiked = isLiked;
    }
}
