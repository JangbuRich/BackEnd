package com.jangburich.domain.store.presentation.dto.response.store;

import com.jangburich.domain.entity.Category;
import com.querydsl.core.annotations.QueryProjection;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public record StoreSearchDetailsResponse(
        String reservable,
        Boolean isLiked,
        String category,
        String address,
        String operationStatus,
        String closeTime,
        String contactNumber,
        String representativeImg,
        List<StoreMenu> storeMenus
) {

    @QueryProjection
    public StoreSearchDetailsResponse(String reservable, Boolean isLiked, String category, String address,
                                      String operationStatus, String closeTime, String contactNumber,
                                      String representativeImg, List<StoreMenu> storeMenus) {
        this.reservable = reservable;
        this.isLiked = isLiked;
        this.category = Category.valueOf(category).getDisplayName();
        this.address = address;
        this.operationStatus = operationStatus;
        this.closeTime = closeTime;
        this.contactNumber = contactNumber;
        this.representativeImg = representativeImg;
        this.storeMenus = storeMenus;
    }


    private static String formatCloseTime(String closeTime) {
        try {
            LocalTime time = LocalTime.parse(closeTime.split("\\.")[0]);
            return time.format(DateTimeFormatter.ofPattern("HH:mm"));
        } catch (Exception e) {
            return closeTime;
        }
    }
}
