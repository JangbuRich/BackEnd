package com.jangburich.presentation.team.dto.response.myTeam;

import com.jangburich.global.payload.PageInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;

@Builder
@Schema(description = "프로필 목록 조회 response DTO")
public record MyTeamResponse(
    List<MyTeamItem> myTeamItem
    , PageInfo pageInfo
) {
}
