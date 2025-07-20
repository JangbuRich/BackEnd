package com.jangburich.domain.entity;

import java.time.LocalDate;

public record OrderResponse(
        Long id,
        String userName,
        String teamName,
        LocalDate date,
        Integer price,
        String status
) {

}
