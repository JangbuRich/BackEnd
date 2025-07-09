package com.jangburich.domain.order.dto.response;

import java.util.List;

public record CartResponse(
        Long storeId,
        String storeName,
        String storeCategory,
        List<GetCartItemsResponse> cartItems,
        Integer totalAmount,
        Integer discountAmount,
        Integer finalAmount
) {
    public static CartResponse of(Long storeId, String storeName, String storeCategory, List<GetCartItemsResponse> cartItems, Integer discountAmount) {
        int totalAmount = cartItems.stream()
                .mapToInt(item -> item.menuPrice() * item.quantity())
                .sum();

        discountAmount = 0;

        int finalAmount = totalAmount - discountAmount;

        return new CartResponse(storeId, storeName, storeCategory, cartItems, totalAmount, discountAmount, finalAmount);
    }

}
