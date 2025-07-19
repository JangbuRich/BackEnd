package com.jangburich.presentation.team.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import java.util.List;

public record IndividualStoreDetailsResponse(
        Long storeId,
        Boolean isMeLeader,
        String storeName,
        boolean isLiked,
        int remainingAmount,
        int availableAmount,
        int myUsedAmount,
        int totalPrepayedAmount,
        int remainingPrepayedAmount,
        int personalUsableAmount,
        String usageStartDate,
        String usageEndDate,
        List<MyPaymentHistory> myPaymentHistories
) {

    @QueryProjection
    public IndividualStoreDetailsResponse(Long storeId, Boolean isMeLeader, String storeName, boolean isLiked,
                                          int remainingAmount, int availableAmount, int myUsedAmount,
                                          int totalPrepayedAmount, int remainingPrepayedAmount,
                                          int personalUsableAmount,
                                          String usageStartDate, String usageEndDate,
                                          List<MyPaymentHistory> myPaymentHistories) {
        this.storeId = storeId;
        this.isMeLeader = isMeLeader;
        this.storeName = storeName;
        this.isLiked = isLiked;
        this.remainingAmount = remainingAmount;
        this.availableAmount = availableAmount;
        this.myUsedAmount = myUsedAmount;
        this.totalPrepayedAmount = totalPrepayedAmount;
        this.remainingPrepayedAmount = remainingPrepayedAmount;
        this.personalUsableAmount = personalUsableAmount;
        this.usageStartDate = usageStartDate;
        this.usageEndDate = usageEndDate;
        this.myPaymentHistories = myPaymentHistories;
    }
}
