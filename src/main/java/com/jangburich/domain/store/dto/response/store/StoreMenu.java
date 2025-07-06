package com.jangburich.domain.store.dto.response.store;

import com.querydsl.core.annotations.QueryProjection;

public record StoreMenu(
        Long menuId,
        String menuName,
        Boolean isSignatureMenu,
        String description,
        int price,
        String menuImgUrl
) {

    @QueryProjection
    public StoreMenu(Long menuId, String menuName, Boolean isSignatureMenu, String description, int price,
                     String menuImgUrl) {
        this.menuId = menuId;
        this.menuName = menuName;
        this.isSignatureMenu = isSignatureMenu;
        this.description = description;
        this.price = price;
        this.menuImgUrl = menuImgUrl;
    }
}
