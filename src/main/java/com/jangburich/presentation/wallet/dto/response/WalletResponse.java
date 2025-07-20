package com.jangburich.presentation.wallet.dto.response;

import com.jangburich.global.payload.PageInfo;

import java.util.List;

public record WalletResponse(
        Integer point,
        String userName,
        List<AvailableOrder> availableOrder,
        PageInfo pageInfo
) {
}
