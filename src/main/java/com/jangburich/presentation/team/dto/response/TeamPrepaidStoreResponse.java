package com.jangburich.presentation.team.dto.response;

import com.jangburich.global.payload.PageInfo;

import java.util.List;

public record TeamPrepaidStoreResponse(List<TeamPrepaidStoreItem> prepaidStoreList, PageInfo pageInfo) {
}
