package com.jangburich.presentation.order.dto.response;

public record GetCartItemsResponse(
        Long menuId,
        String menuImg,
        String menuName,
        String menuDescription,
        Integer quantity,
        Integer menuPrice
) {
    public static GetCartItemsResponse of(Long menuId, String menuImg, String menuName, String menuDescription, Integer quantity,
                                          Integer menuPrice) {
        return new GetCartItemsResponse(menuId, menuImg, menuName, menuDescription, quantity, menuPrice);
    }
}
