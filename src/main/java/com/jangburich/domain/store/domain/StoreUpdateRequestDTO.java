package com.jangburich.domain.store.domain;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class StoreUpdateRequestDTO {
	@Enumerated(EnumType.STRING)
	private Category category;
	private String representativeImage;
	private Boolean reservationAvailable;
	private Long maxReservation;
	private Long minPrepayment;
	private Long prepaymentDuration;
	private String introduction;
	private Double latitude;
	private Double longitude;
	private String address;
	private String location;
	private List<DayOfWeek> dayOfWeek;
	private LocalTime openTime;
	private LocalTime closeTime;
}
