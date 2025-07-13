package com.jangburich.presentation.store.dtos.response.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.jangburich.domain.entity.OrderStatus;
import com.jangburich.domain.entity.Orders;

public record OrderDetailResponse(
	Long id,
	String teamName,
	String teamUserName,
	LocalDateTime dateTime,
	OrderStatus orderStatus,
	Integer amount,
	Integer totalPrice,
	BigDecimal discountPrice
) {

	public static OrderDetailResponse of(Orders order) {
		return new OrderDetailResponse(
			order.getId(),
			order.getTeam().getName(),
			order.getUser().getName(),
			order.getUpdatedAt(),
			order.getOrderStatus(),
			order.getOrderPrice(), // 상품 금액 -> 어떻게 분리할지 고민중
			order.getOrderPrice(), // 합계 금액
			order.getDiscountPrice()
		);
	}

}


