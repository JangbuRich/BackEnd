package com.jangburich.domain.order.domain.repository;

import java.time.LocalDateTime;
import java.util.List;

import com.jangburich.domain.order.domain.Orders;

public interface CustomOrderRepository {
    List<Orders> queryOrdersByStoreIdAndStartDateAndEndDate(Long storeId, LocalDateTime startDate, LocalDateTime endDate);
}
