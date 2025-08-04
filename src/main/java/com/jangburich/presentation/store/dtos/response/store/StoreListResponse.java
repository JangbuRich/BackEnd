package com.jangburich.presentation.store.dtos.response.store;

import com.jangburich.global.payload.PageInfo;

import java.util.List;

public record StoreListResponse(List<StoreListItem> storeList, PageInfo pageInfo) {
}
