package com.jangburich.domain.store.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PrepaymentInfoResponse {
	private Long minPrepayAmount;
	private Integer wallet;
	private Integer remainPrepay;
	private String category;
	private String storeName;

	@Builder
	public PrepaymentInfoResponse(Long minPrepayAmount, Integer wallet, Integer remainPrepay, String category, String storeName) {
		this.minPrepayAmount = minPrepayAmount;
		this.wallet = wallet;
		this.remainPrepay = remainPrepay;
		this.category = category;
		this.storeName = storeName;
	}
}
