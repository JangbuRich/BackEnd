package com.jangburich.presentation.team.dto.response.myTeam;

import io.swagger.v3.oas.annotations.media.Schema;

public record MyTeamBaseItem(
        @Schema(description = "그룹 id")
        Long teamId,
        @Schema(description = "그룹 이름")
        String teamName,
        @Schema(description = "잔액")
        Integer balance,
        @Schema(description = "그룹 카테고리")
        String teamType,
        @Schema(description = "관심 매장 여부")
        Boolean isLiked,
        @Schema(description = "그룹 인원 수")
        Integer memberCount,
        @Schema(description = "그룹 대표 여부")
        Boolean isLeader
) {
}
