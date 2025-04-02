package com.jangburich.domain.user.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import java.util.List;

public record UserHomeResponse(
        Long userId,
        String currentDate,
        String userName,
        List<TeamsResponse> teams,
        int joinedTeamCount,
        int reservationCount
) {

    @QueryProjection
    public UserHomeResponse(Long userId, String currentDate, String userName, List<TeamsResponse> teams,
                            int joinedTeamCount, int reservationCount) {
        this.userId = userId;
        this.currentDate = currentDate;
        this.userName = userName;
        this.teams = teams;
        this.joinedTeamCount = joinedTeamCount;
        this.reservationCount = reservationCount;
    }
}
