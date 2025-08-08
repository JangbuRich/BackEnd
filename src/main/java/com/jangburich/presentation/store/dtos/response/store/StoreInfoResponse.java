package com.jangburich.presentation.store.dtos.response.store;

import com.jangburich.domain.entity.Category;
import com.jangburich.domain.entity.Store;
import com.jangburich.domain.store.StoreMenu;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.DayOfWeek;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public class StoreInfoResponse {
    private Long id;
    private String ownerId;
    private String name;
    private String phoneNumber;
    private String uniqueCode;

    @Enumerated(EnumType.STRING)
    private Category category;

    private String representativeImage;
    private String description;
    private String address;
    private String location;
    private String dayOfWeek;
    private String openTime;
    private String closeTime;
    private List<StoreMenu> menuUrlList;

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");


    public StoreInfoResponse(Long id, String ownerId, String name, String phoneNumber, String uniqueCode, Category category, String representativeImage, String description, String address, String location, String dayOfWeek, String openTime, String closeTime, List<StoreMenu> menuUrlList) {
        this.id = id;
        this.ownerId = ownerId;
        this.name = name;
        this.uniqueCode = uniqueCode;
        this.category = category;
        this.representativeImage = representativeImage;
        this.description = description;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.location = location;
        this.dayOfWeek = dayOfWeek;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.menuUrlList = menuUrlList;
    }

    private String convertDayOfWeekToKorean(DayOfWeek dayOfWeek) {
        return switch (dayOfWeek) {
            case MONDAY -> "월";
            case TUESDAY -> "화";
            case WEDNESDAY -> "수";
            case THURSDAY -> "목";
            case FRIDAY -> "금";
            case SATURDAY -> "토";
            case SUNDAY -> "일";
        };
    }

    public StoreInfoResponse of(Store store) {
        String dayOfWeekString = store.getWorkDays().stream()
                .map(this::convertDayOfWeekToKorean)
                .collect(Collectors.joining(", "));

        return new StoreInfoResponse(
                store.getId(),
                store.getOwner().getUser().getProviderId(),
                store.getName(),
                store.getContactNumber(),
                store.getStoreUniqueCode(),
                store.getCategory(),
                store.getRepresentativeImage(),
                store.getIntroduction(),
                store.getAddress(),
                store.getLocation(),
                dayOfWeekString,
                store.getOpenTime().format(TIME_FORMATTER),
                store.getCloseTime().format(TIME_FORMATTER),
                store.getStoreMenus()
        );

    }
}
