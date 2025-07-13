package com.jangburich.presentation.wallet.dto.response;

import java.util.List;

public record WalletResponse(
        Integer point,
        List<PurchaseHistory> purchaseHistories
) {
}
