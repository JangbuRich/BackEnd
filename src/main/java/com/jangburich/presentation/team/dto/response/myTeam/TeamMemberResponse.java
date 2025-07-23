package com.jangburich.presentation.team.dto.response.myTeam;

import com.jangburich.global.payload.PageInfo;

import java.util.List;

public record TeamMemberResponse(List<TeamMemberItem> teamMemberItemList, PageInfo pageInfo) {
}
