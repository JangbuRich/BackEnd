package com.jangburich.presentation.store.dtos.request;

import com.jangburich.domain.entity.Category;
import com.jangburich.domain.store.StoreMenu;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class StoreUpdateRequest {
 	@Enumerated(EnumType.STRING)
	private Category category;
	private String representativeImage;
	private String introduction;
	private String address;
	private String location;
	private String phoneNumber;
	private String uniqueCode;
	private List<DayOfWeek> dayOfWeek;
	private LocalTime openTime;
	private LocalTime closeTime;
	private List<StoreMenu> storeMenus;
}