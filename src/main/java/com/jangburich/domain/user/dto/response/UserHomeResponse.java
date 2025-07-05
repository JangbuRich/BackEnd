package com.jangburich.domain.user.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import java.util.List;

public record UserHomeResponse(
        Long userId,
        String currentDate,
        String userName,
        List<TeamsResponse> teams,
        String lastVisitedStore,
        int joinedTeamCount
) {

    @QueryProjection
    public UserHomeResponse(Long userId, String currentDate, String userName, List<TeamsResponse> teams,
                            String lastVisitedStore, int joinedTeamCount) {
        this.userId = userId;
        this.currentDate = currentDate;
        this.userName = userName;
        this.teams = teams;
        this.lastVisitedStore = lastVisitedStore;
        this.joinedTeamCount = joinedTeamCount;
    }
}
