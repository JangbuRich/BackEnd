package com.jangburich.presentation.team.dto.response;

import com.jangburich.domain.common.Status;
import com.jangburich.domain.entity.TeamType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class TeamSecretCodeResponse {
	private String teamName;
	private LocalDateTime createdAt;
	private TeamType teamType;
	private Long teamMembers;
	private List<String> teamMemberProfileImages;
	private Status status;

	@Builder
	public TeamSecretCodeResponse(String teamName, LocalDateTime createdAt, TeamType teamType, Long teamMembers,
								  List<String> teamMemberProfileImages, Status status) {
		this.teamName = teamName;
		this.createdAt = createdAt;
		this.teamType = teamType;
		this.teamMembers = teamMembers;
		this.teamMemberProfileImages = teamMemberProfileImages;
		this.status = status;
	}
}
