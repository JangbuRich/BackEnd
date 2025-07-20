package com.jangburich.application.order.service;

import com.jangburich.domain.entity.Orders;
import com.jangburich.infrastructure.repository.OrdersRepository;
import com.jangburich.domain.user.domain.User;
import com.jangburich.global.error.DefaultException;
import com.jangburich.global.payload.ErrorCode;
import com.jangburich.infrastructure.repository.UserRepository;
import com.jangburich.presentation.order.dto.response.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderQueryService {

    private final OrdersRepository ordersRepository;
    private final UserRepository userRepository;

    public OrderResponse getOrder(String userProviderId, long orderId) {
        User user = userRepository.findByProviderId(userProviderId)
                .orElseThrow(() -> new DefaultException(ErrorCode.INVALID_USER_ID));

        Orders orders = ordersRepository.findById(orderId)
                .orElseThrow(() -> new DefaultException(ErrorCode.INVALID_ORDER_ID));

        return new OrderResponse(orderId, orders.getStore().getId(), orders.getStore().getName(), orders.getOrderPrice(), orders.getOrderStatus());
    }
}
