package com.jangburich.presentation.wallet.dto.response;

import com.jangburich.domain.entity.OrderStatus;
import com.jangburich.global.payload.PageInfo;
import org.springframework.data.domain.Pageable;

import java.util.List;

public record PointResponse(
        List<PointTransactionItem> transactionList
        , PageInfo pageInfo
){
}