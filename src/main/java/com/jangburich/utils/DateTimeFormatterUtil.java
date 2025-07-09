package com.jangburich.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.springframework.stereotype.Component;

@Component
public class DateTimeFormatterUtil {

    private static final DateTimeFormatter KOREAN_DATE_TIME_FORMATTER =
        DateTimeFormatter.ofPattern("yyyy.MM.dd a h:mm", Locale.KOREA);

    private static final DateTimeFormatter KOREAN_DATE_FORMATTER =
        DateTimeFormatter.ofPattern("yyyy.MM.dd", Locale.KOREA);

    private static final DateTimeFormatter KOREAN_TIME_FORMATTER =
        DateTimeFormatter.ofPattern("a h:mm", Locale.KOREA);

    public static String formatToKoreanDateTime(LocalDateTime dateTime) {
        return dateTime.format(KOREAN_DATE_TIME_FORMATTER);
    }

    public static String formatToKoreanDate(LocalDate date) {
        return date.format(KOREAN_DATE_FORMATTER);
    }

    public static String formatToKoreanTime(LocalTime time) {
        return time.format(KOREAN_TIME_FORMATTER);
    }

}
