package com.jangburich.domain.entity;

public enum TransactionType {
    POINT_PURCHASE("지갑 충전"),
    FOOD_PURCHASE("제휴 매장 음식 구매"),
    REFUND("포인트 환불"),
    PREPAY("제휴 매장 선결제 금액 충전"),
    PREPAY_REFUND("제휴 매장 선결제 금액 환불"),
    FOOD_REFUND("제휴 매장 음식 환불");

    private final String displayName;

    TransactionType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}