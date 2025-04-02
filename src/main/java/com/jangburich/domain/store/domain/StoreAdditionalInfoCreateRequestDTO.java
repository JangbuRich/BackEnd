package com.jangburich.domain.store.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class StoreAdditionalInfoCreateRequestDTO {
	private Boolean reservationAvailable;
	private Long maxReservation;
	private Long minPrepayment;
	private Long prepaymentDuration;
}
