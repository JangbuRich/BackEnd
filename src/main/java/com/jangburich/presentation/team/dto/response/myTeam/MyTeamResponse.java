package com.jangburich.presentation.team.dto.response.myTeam;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;

@Builder
@Schema(description = "프로필 목록 조회 response DTO")
public record MyTeamResponse(
    MyTeamItem myTeamItem
    , PageInfo pageInfo
) {
}
