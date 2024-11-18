package com.jangburich.domain.payment.application;

import com.jangburich.domain.payment.application.strategy.PaymentServiceStrategy;
import com.jangburich.domain.payment.dto.request.PayRequest;
import com.jangburich.domain.payment.dto.response.ApproveResponse;
import com.jangburich.domain.payment.dto.response.ReadyResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentProcessingService {

    private final PaymentServiceStrategy paymentServiceStrategy;

    public ReadyResponse processPayment() {
        PaymentService paymentService = paymentServiceStrategy.getPaymentService("kakao");
        return paymentService.payReady();
    }

    public ApproveResponse processSuccess(String tid, String pgToken) {
        PaymentService paymentService = paymentServiceStrategy.getPaymentService("kakao");
        return paymentService.payApprove(tid, pgToken);
    }
}
