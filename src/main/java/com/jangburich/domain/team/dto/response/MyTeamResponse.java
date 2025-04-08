package com.jangburich.domain.team.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;

@Builder
@Schema(description = "프로필 목록 조회 response DTO")
public record MyTeamResponse(
    @Schema(description = "그룹 id")
    Long teamId,
    @Schema(description = "그룹 이름")
    String teamName,
    @Schema(description = "그룹 카테고리")
    String teamType,
    @Schema(description = "관심 매장 여부")
    Boolean isLiked,
    @Schema(description = "그룹 인원 수")
    Integer memberCount,
    @Schema(description = "그룹 대표 여부")
    Boolean isLeader,
    @Schema(description = "멤버 프로필 사진 (최대 3명)")
    List<String> profileImageUrl,
    @Schema(description = "남은 돈")
    Integer availableAmount
) {
}
