package com.jangburich.presentation.team.dto.response.myTeam;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;

import java.util.List;

public record MyTeamDetailResponse(Long teamId, Boolean isMeLeader, String teamName, String description,
                                   int totalPrepaidAmount, int remainingAmount, String storeName,
                                   int storePrepaidAmount, int storeRemainingAmount, int userUsedAmount,
                                   List<String> teamMemberImgUrl, int totalMemberCount) {

    @Builder
    @QueryProjection
    public MyTeamDetailResponse(Long teamId, Boolean isMeLeader, String teamName, String description, int totalPrepaidAmount, int remainingAmount, String storeName, int storePrepaidAmount, int storeRemainingAmount, int userUsedAmount, List<String> teamMemberImgUrl, int totalMemberCount) {
        this.teamId = teamId;
        this.isMeLeader = isMeLeader;
        this.teamName = teamName;
        this.description = description;
        this.totalPrepaidAmount = totalPrepaidAmount;
        this.remainingAmount = remainingAmount;
        this.storeName = storeName;
        this.storePrepaidAmount = storePrepaidAmount;
        this.storeRemainingAmount = storeRemainingAmount;
        this.userUsedAmount = userUsedAmount;
        this.teamMemberImgUrl = teamMemberImgUrl;
        this.totalMemberCount = totalMemberCount;
    }
}
