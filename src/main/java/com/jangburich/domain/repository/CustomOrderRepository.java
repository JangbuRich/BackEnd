package com.jangburich.domain.repository;

import java.time.LocalDateTime;
import java.util.List;

import com.jangburich.domain.entity.Orders;

public interface CustomOrderRepository {
    List<Orders> queryOrdersByStoreIdAndStartDateAndEndDate(Long storeId, LocalDateTime startDate, LocalDateTime endDate);
}
