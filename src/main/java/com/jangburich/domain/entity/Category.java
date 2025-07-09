package com.jangburich.domain.store.domain;

import java.util.Arrays;

public enum Category {
    ALL("전체"),
    KOREAN("한식"),
    CHINESE("중식"),
    JAPANESE("일식"),
    WESTERN("양식"),
    SNACK("분식"),
    MEAT("육류"),
    SEAFOOD("해산물"),
    VIETNAMESE("베트남"),
    MEXICAN("멕시코"),
    INDIAN("인도"),
    THAI("태국"),
    OTHER("기타");

    private final String displayName;

    Category(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static Category fromDisplayName(String displayName) {
        return Arrays.stream(Category.values())
            .filter(category -> category.getDisplayName().equals(displayName))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Invalid category: " + displayName + ". Please use one of: " + getValidCategories()));
    }

    private static String getValidCategories() {
        return Arrays.stream(Category.values())
            .map(Category::getDisplayName)
            .reduce((a, b) -> a + ", " + b)
            .orElse("");
    }

}
