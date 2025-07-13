package com.jangburich.domain.store.exception;

import com.jangburich.global.payload.ErrorCode;

import lombok.Getter;

@Getter
public class OrdersNotFoundException extends RuntimeException {

	private final ErrorCode errorCode;

	public OrdersNotFoundException(ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}
}
