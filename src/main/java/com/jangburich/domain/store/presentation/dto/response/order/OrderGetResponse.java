package com.jangburich.domain.store.presentation.dto.response.order;

import com.jangburich.domain.entity.OrderStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderGetResponse {
	private Long id; // order id(sequence)?
	private OrderStatus orderStatus;
	private String name;
	private String teamName;
	private Integer price;

}
