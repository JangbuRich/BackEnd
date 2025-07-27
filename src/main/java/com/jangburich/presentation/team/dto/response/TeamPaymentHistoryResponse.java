package com.jangburich.presentation.team.dto.response;

import com.jangburich.global.payload.PageInfo;

import java.time.LocalDateTime;
import java.util.List;

public record TeamPaymentHistoryResponse(LocalDateTime startDate, LocalDateTime endDate,
                                         List<TeamPaymentHistoryItem> teamPaymentHistoryList, PageInfo pageInfo) {
}
