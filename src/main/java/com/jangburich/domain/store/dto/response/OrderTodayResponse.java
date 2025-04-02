package com.jangburich.domain.store.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class OrderTodayResponse {
	private Integer totalPrice;
	private List<OrderGetResponse> orders;

	@Builder
	public OrderTodayResponse(Integer totalPrice, List<OrderGetResponse> orders) {
		this.totalPrice = totalPrice;
		this.orders = orders;
	}
}
