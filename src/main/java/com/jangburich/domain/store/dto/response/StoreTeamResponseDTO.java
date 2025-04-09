package com.jangburich.domain.store.dto.response;

import java.time.LocalDateTime;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Builder;

@Builder
public record StoreTeamResponseDTO(
		Long id,
		Integer remainPoint,
		Integer point,
		Long teamId,
		String teamName,
		String teamDescription,
		Long storeId,
		LocalDateTime updatedAt

) {
	@QueryProjection
	public StoreTeamResponseDTO(Long id, Integer remainPoint, Integer point, Long teamId, String teamName,
		String teamDescription, Long storeId, LocalDateTime updatedAt) {
		this.id = id;
		this.remainPoint = remainPoint;
		this.point = point;
		this.teamId = teamId;
		this.teamName = teamName;
		this.teamDescription = teamDescription;
		this.storeId = storeId;
		this.updatedAt = updatedAt;
	}
}