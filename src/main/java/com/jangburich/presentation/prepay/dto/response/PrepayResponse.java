package com.jangburich.presentation.prepay.dto.response;

import com.jangburich.domain.point.domain.TransactionType;

import lombok.Builder;

public final class PrepayResponse {

    @Builder
    public static class StorePrepayInfo {
        private Long pointTransactionId;
        private String teamName;
        private String userName;
        private Integer transactionPoint;
        private TransactionType transactionType;
    }
}
