package com.jangburich.domain.team.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import java.util.List;

public record MyTeamDetailResponse(
        Long teamId,
        Boolean isMeLeader,
        String storeName,
        String teamName,
        String description,
        int totalPrepaidAmount,
        int remainingAmount,
        int personalAllocatedAmount,
        int userUsedAmount,
        List<PrepayedStore> prepayedStores,
        List<String> teamMemberImgUrl,
        int totalMemberCount,
        List<TodayPayment> todayPayments,
        int totalTodayTransactionCount
) {

    @QueryProjection
    public MyTeamDetailResponse(Long teamId, Boolean isMeLeader, String storeName, String teamName, String description,
                                int totalPrepaidAmount, int remainingAmount, int personalAllocatedAmount,
                                int userUsedAmount, List<PrepayedStore> prepayedStores, List<String> teamMemberImgUrl,
                                int totalMemberCount, List<TodayPayment> todayPayments,
                                int totalTodayTransactionCount) {
        this.teamId = teamId;
        this.isMeLeader = isMeLeader;
        this.storeName = storeName;
        this.teamName = teamName;
        this.description = description;
        this.totalPrepaidAmount = totalPrepaidAmount;
        this.remainingAmount = remainingAmount;
        this.personalAllocatedAmount = personalAllocatedAmount;
        this.userUsedAmount = userUsedAmount;
        this.prepayedStores = prepayedStores;
        this.teamMemberImgUrl = teamMemberImgUrl;
        this.totalMemberCount = totalMemberCount;
        this.todayPayments = todayPayments;
        this.totalTodayTransactionCount = totalTodayTransactionCount;
    }
}
