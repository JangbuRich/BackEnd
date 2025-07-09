package com.jangburich.presentation.store.dtos.condition;

import com.querydsl.core.annotations.QueryProjection;

public record StoreSearchConditionWithType(
        Boolean isReservable,
        Boolean isOperational,
        Boolean isPrepaid,
        Boolean isPostpaid
) {

    @QueryProjection
    public StoreSearchConditionWithType(Boolean isReservable, Boolean isOperational, Boolean isPrepaid,
                                        Boolean isPostpaid) {
        this.isReservable = isReservable;
        this.isOperational = isOperational;
        this.isPrepaid = isPrepaid;
        this.isPostpaid = isPostpaid;
    }
}