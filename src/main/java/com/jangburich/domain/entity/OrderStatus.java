package com.jangburich.domain.entity;

import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum OrderStatus {
    SUCCESS("사용완료"),
    USE_CANCELED("사용취소"),
    PAYMENT_CANCELED("결제취소"),

    RECEIVED("식권 발급 완료"),
    TICKET_ISSUED("금액권 발급 완료"),
    TICKET_USED("식권 사용 완료"),
    CANCELLED("사용 취소")
    ;

    private final String description;

    public static List<OrderStatus> PaymentStatus() {
        return List.of(SUCCESS, PAYMENT_CANCELED, CANCELLED);
    }
}
