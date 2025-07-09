package com.jangburich.presentation.store.dtos.response.order;

import java.util.List;

public record OrderTodayResponse(
	List<OrderGetResponse> orders) {

	public static OrderTodayResponse of(List<OrderGetResponse> orders) {
		return new OrderTodayResponse(orders);
	}
}
