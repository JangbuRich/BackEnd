package com.jangburich.utils;

import java.time.DayOfWeek;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DayOfWeekConverter {
	private DayOfWeekConverter() {

	}

	public static DayOfWeek convertKoreanToDayOfWeek(String koreanDay) {
		return switch (koreanDay.trim()) {
			case "월" -> DayOfWeek.MONDAY;
			case "화" -> DayOfWeek.TUESDAY;
			case "수" -> DayOfWeek.WEDNESDAY;
			case "목" -> DayOfWeek.THURSDAY;
			case "금" -> DayOfWeek.FRIDAY;
			case "토" -> DayOfWeek.SATURDAY;
			case "일" -> DayOfWeek.SUNDAY;
			default -> throw new IllegalArgumentException("Invalid day: " + koreanDay);
		};
	}

	public static List<DayOfWeek> convertStringToDayOfWeekList(String days) {
		return Arrays.stream(days.split(","))
			.map(DayOfWeekConverter::convertKoreanToDayOfWeek)
			.collect(Collectors.toList());
	}
}
