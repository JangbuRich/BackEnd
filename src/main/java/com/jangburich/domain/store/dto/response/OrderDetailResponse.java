package com.jangburich.domain.store.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class OrderDetailResponse {
	private Long id;
	private String teamName;
	private String teamUserName;
	private LocalDateTime dateTime;
	private Integer amount;
	private Integer totalPrice;
	private Integer discountPrice;

	@Builder
	public OrderDetailResponse(Long id, String teamName, String teamUserName, LocalDateTime dateTime, Integer amount, Integer totalPrice, Integer discountPrice) {
		this.id = id;
		this.teamName = teamName;
		this.teamUserName = teamUserName;
		this.dateTime = dateTime;
		this.amount = amount;
		this.totalPrice = totalPrice;
		this.discountPrice = discountPrice;
	}
}

