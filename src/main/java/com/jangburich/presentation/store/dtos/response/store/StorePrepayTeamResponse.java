package com.jangburich.presentation.store.dtos.response.store;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.jangburich.domain.prepay.enums.PrepayStatus;

import lombok.Builder;
import lombok.Getter;

public final class StorePrepayTeamResponse {

    @Builder
    @Getter
    public static class teamInfo {
        private String teamName;
        private String teamDescription;
        private BigDecimal prepayAmount;
    }

    @Builder
    @Getter
    public static class ApprovalDetail {
        private String teamName;
        private String userName;
        private LocalDateTime registerTime;
        private BigDecimal prepayAmount;
    }

    @Builder
    @Getter
    public static class approvalResponse {
        private BigDecimal prepayAmount;
        private PrepayStatus prepayStatus;
        private LocalDateTime approvalTime;
    }

}
