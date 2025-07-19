package com.jangburich.presentation.team.dto.response;

public record TeamMemberResponse(
        Long memberId,
        String memberName,
        Boolean isMe,
        Boolean isLeader,
        String profileImgUrl
) {
}
