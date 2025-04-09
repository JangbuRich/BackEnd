package com.jangburich.domain.store.dto.response;

import java.time.LocalDateTime;

import com.jangburich.domain.point.domain.TransactionType;
import com.querydsl.core.annotations.QueryProjection;

public record StoreChargeHistoryResponse(
	Long id,
	LocalDateTime createdAt,
	String teamName,
	Long teamId,
	Integer transactionedPoint,
	TransactionType transactionType
) {
	@QueryProjection
	public StoreChargeHistoryResponse(Long id, LocalDateTime createdAt, String teamName, Long teamId,
		Integer transactionedPoint, TransactionType transactionType) {
		this.id = id;
		this.createdAt = createdAt;
		this.teamName = teamName;
		this.teamId = teamId;
		this.transactionedPoint = transactionedPoint;
		this.transactionType = transactionType;
	}
}
