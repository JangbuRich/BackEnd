package com.jangburich.domain.store.presentation.dto.response.store;

import lombok.Builder;
import lombok.Getter;

public final class StoreResponse {

    @Builder
    @Getter
    public static class AccountInfo {
        private String today;
        private Integer todayTotalOrderCount;
        private Integer todayTotalOrderPrice;
        private Integer totalPrepayPrice; // 총 선결제 금액
        private Integer newPrepayPrice; // 신규 선결제 금액
        private Integer newPrepayGroup; // 신규 선결제 그룹
    }

}
