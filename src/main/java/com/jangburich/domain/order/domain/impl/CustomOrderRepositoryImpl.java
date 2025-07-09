package com.jangburich.domain.order.domain.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.jangburich.domain.order.domain.Orders;
import com.jangburich.domain.order.domain.QOrders;
import com.jangburich.domain.order.domain.repository.CustomOrderRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class CustomOrderRepositoryImpl implements CustomOrderRepository {
    private static final QOrders Q_ORDERS = QOrders.orders;

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Orders> queryOrdersByStoreIdAndStartDateAndEndDate (Long storeId, LocalDateTime startDate, LocalDateTime endDate) {
        return jpaQueryFactory.selectFrom(Q_ORDERS)
            .where(Q_ORDERS.store.id.eq(storeId)
                .and(Q_ORDERS.createdAt.between(startDate, endDate))
            ).fetch();
    }

}
