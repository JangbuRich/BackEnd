package com.jangburich.presentation.store.dtos.response.store;

import com.jangburich.domain.entity.Category;
import com.querydsl.core.annotations.QueryProjection;

import java.util.List;

public record StoreDetailsResponse(Long id, String storeName, Boolean isLiked, Boolean groupAvailable, Boolean isLeader,
                                   String category, String address, String closeTime, String contactNumber,
                                   String representativeImg, List<String> storeMenus) {

    @QueryProjection
    public StoreDetailsResponse(Long id, String storeName, Boolean isLiked, Boolean groupAvailable, Boolean isLeader, String category, String address, String closeTime, String contactNumber, String representativeImg, List<String> storeMenus) {
        this.id = id;
        this.storeName = storeName;
        this.isLiked = isLiked;
        this.groupAvailable = groupAvailable;
        this.isLeader = isLeader;
        this.category = Category.valueOf(category).getDisplayName();
        this.address = address;
        this.closeTime = closeTime;
        this.contactNumber = contactNumber;
        this.representativeImg = representativeImg;
        this.storeMenus = storeMenus;
    }
}
