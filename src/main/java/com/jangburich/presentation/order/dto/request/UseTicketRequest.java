package com.jangburich.presentation.order.dto.request;

public record UseTicketRequest(
        Long storeId
        , Long teamId
        , String secretCode
) {

}
