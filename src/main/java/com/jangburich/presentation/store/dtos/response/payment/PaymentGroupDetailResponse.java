package com.jangburich.presentation.store.dtos.response.payment;

import java.util.List;

import com.jangburich.domain.entity.OrderResponse;
import com.jangburich.domain.entity.Team;
import com.jangburich.domain.user.domain.User;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PaymentGroupDetailResponse {
    private String teamName;
    private String teamDescription;
    private Integer todayUsedPoint;
    private Integer remainPoint;
    private String teamLeaderName;
    private String teamLeaderPhoneNum;
    private String teamLeaderProfileImageUrl;
    private List<OrderResponse> historyChargeResponses;
    private List<OrderResponse> historyPaymentResponses;
    // 차감 결제 내역을 조회한다. -> 충전 금액에서 차감된 내역을 본다.

    public static PaymentGroupDetailResponse create(Team team, Integer todayUsedPoint, Integer remainPoint,
                                                    User teamLeader, List<OrderResponse> historyChargeResponses, List<OrderResponse> historyPaymentResponses) {
        PaymentGroupDetailResponse paymentGroupDetailResponse = new PaymentGroupDetailResponse();
        paymentGroupDetailResponse.teamName = team.getName();
        paymentGroupDetailResponse.teamDescription = team.getDescription();
        paymentGroupDetailResponse.todayUsedPoint = todayUsedPoint;
        paymentGroupDetailResponse.remainPoint = remainPoint;
        paymentGroupDetailResponse.teamLeaderName = teamLeader.getNickname();
        paymentGroupDetailResponse.teamLeaderPhoneNum = teamLeader.getPhoneNumber();
        paymentGroupDetailResponse.teamLeaderProfileImageUrl = teamLeader.getProfileImageUrl();
        paymentGroupDetailResponse.historyChargeResponses = historyChargeResponses;
        paymentGroupDetailResponse.historyPaymentResponses = historyPaymentResponses;
        return paymentGroupDetailResponse;
    }
}
