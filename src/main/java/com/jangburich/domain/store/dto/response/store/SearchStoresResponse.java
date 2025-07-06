package com.jangburich.domain.store.dto.response.store;

import com.jangburich.domain.store.domain.Category;
import com.querydsl.core.annotations.QueryProjection;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import lombok.Builder;

@Builder
public record SearchStoresResponse(
	Long storeId,
	String name,
	Double latitude,
	Double longitude,
	Boolean isFavorite,
	String category,
	Double distance,
	String businessStatus,
	String closeTime,
	String phoneNumber,
	String imageUrl
) {

	@QueryProjection
	public SearchStoresResponse(Long storeId, String name, Double latitude, Double longitude, Boolean isFavorite,
		Category category, Double distance, String businessStatus, String closeTime,
		String phoneNumber, String imageUrl) {
		this(
			storeId,
			name,
			latitude,
			longitude,
			isFavorite,
			category.getDisplayName(),
			distance,
			businessStatus,
			formatCloseTime(closeTime),
			phoneNumber,
			imageUrl
		);
	}

	private static String formatCloseTime(String closeTime) {
		try {
			LocalTime time = LocalTime.parse(closeTime.split("\\.")[0]);
			return time.format(DateTimeFormatter.ofPattern("HH:mm"));
		} catch (Exception e) {
			return closeTime;
		}
	}
}
