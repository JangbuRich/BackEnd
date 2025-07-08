package com.jangburich.domain.store.presentation.dto.response.order;

import java.util.List;

public record OrderTodayResponse(
	List<OrderGetResponse> orders) {

	public static OrderTodayResponse of(List<OrderGetResponse> orders) {
		return new OrderTodayResponse(orders);
	}
}
