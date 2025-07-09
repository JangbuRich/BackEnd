package com.jangburich.presentation.store.dtos.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class StoreAdditionalInfoCreateRequest {
	private Boolean reservationAvailable;
	private Long maxReservation;
	private Long minPrepayment;
	private Long prepaymentDuration;
}
