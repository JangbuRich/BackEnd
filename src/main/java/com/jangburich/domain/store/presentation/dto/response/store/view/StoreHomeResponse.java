package com.jangburich.domain.store.presentation.dto.response.store.view;

import java.util.List;

import com.jangburich.domain.store.presentation.dto.response.order.OrderGetResponse;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class StoreHomeResponse {

    @Getter
    @Builder
    public static class TodayPaymentTeam {
        private String teamName;
        private String teamDescription;
        private Integer remainingPrice;
    }

    @Builder
    @Getter
    public static class UniqueCode {
        private final String uniqueCode;
    }

    @Builder
    @Getter
    public static class TodayOrder {
        private List<OrderGetResponse> orders;

        public static TodayOrder of(List<OrderGetResponse> orders) {
            return new TodayOrder(orders);
        }
    }

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
