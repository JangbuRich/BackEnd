package com.jangburich.presentation.wallet.dto.response;

import com.jangburich.global.payload.PageInfo;

import java.util.List;

public record PointTransactionList(
        List<PointTransactionItem> transactionList
        , PageInfo pageInfo
){
}