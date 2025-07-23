package com.jangburich.presentation.team.dto.response.myTeam;

public record TeamMemberItem(Long memberId, String memberName, Boolean isMe, Boolean isLeader, String profileImgUrl) {
}
