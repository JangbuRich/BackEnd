package com.jangburich.presentation.team.dto.response;

import com.querydsl.core.annotations.QueryProjection;

public record TodayPayment(
        String currentDate,
        String transactionTime,
        String menuName,
        String consumeUserName,
        int useAmount
) {

    @QueryProjection
    public TodayPayment(String currentDate, String transactionTime, String menuName, String consumeUserName,
                        int useAmount) {
        this.currentDate = currentDate;
        this.transactionTime = transactionTime;
        this.menuName = menuName;
        this.consumeUserName = consumeUserName;
        this.useAmount = useAmount;
    }
}
