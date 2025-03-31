package com.jangburich.domain.store.dto.response;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class OrderGetResponse {
	private Long id;
	private String menuNames;
	private LocalDateTime date;
	private Integer count;
	private Integer price;

	@Builder
	public OrderGetResponse(Long id, String menuNames, LocalDateTime date, Integer count, Integer price) {
		this.id = id;
		this.menuNames = menuNames;
		this.date = date;
		this.count = count;
		this.price = price;
	}
}
