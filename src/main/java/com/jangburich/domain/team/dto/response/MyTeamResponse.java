package com.jangburich.domain.team.dto.response;

import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
public record MyTeamResponse(
        Long teamId,
        String teamName,
        String processState,
        LocalDate createdDate,
        String teamType,
        Boolean isLiked,
        int peopleCount,
        Boolean isMeLeader,
        List<String> profileImageUrl
) {
}
