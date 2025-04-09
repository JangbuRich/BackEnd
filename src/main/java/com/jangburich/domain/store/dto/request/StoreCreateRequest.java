package com.jangburich.domain.store.dto.request;

import com.jangburich.domain.store.domain.Category;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Getter
@RequiredArgsConstructor
public class StoreCreateRequest {

	private String storeName;
	private String phoneNumber;
	private String businessName;
	private String businessRegistrationNumber;
	private LocalDate openingDate;
	private Boolean agreeMarketing;
	private Boolean agreeAdvertise;

	private String introduction;

	@Enumerated(EnumType.STRING)
	private Category category;

	private Double latitude;
	private Double longitude;
	private String address;
	private String location;

	private String dayOfWeek;
	private String openTime;
	private String closeTime;

	private Long minPrepayment;
	private Long prepaymentDuration;
	private Boolean reservationAvailable;
	private Long maxReservation;

	private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

	public void setOpenTime(LocalTime openTime) {
		this.openTime = openTime != null ? openTime.format(TIME_FORMATTER) : null;
	}

	public void setCloseTime(LocalTime closeTime) {
		this.closeTime = closeTime != null ? closeTime.format(TIME_FORMATTER) : null;
	}
}



