package com.jangburich.domain.order.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Getter
@RequiredArgsConstructor
public class OrderResponse {
	private Long id;
	private String userName;
	private LocalDate date;
	private String price;

	public OrderResponse(Long id, String userName, LocalDate date, String price) {
		this.id = id;
		this.userName = userName;
		this.date = date;
		this.price = price;
	}
}
