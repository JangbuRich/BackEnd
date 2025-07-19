package com.jangburich.presentation.user.dto.response;

import com.jangburich.global.payload.PageInfo;

import java.util.List;

public record StoreListResponse(
        List<StoreItem> storeItem
        , PageInfo pageInfo
) {
}
