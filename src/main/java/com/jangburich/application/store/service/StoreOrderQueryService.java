package com.jangburich.application.store.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jangburich.application.store.resolver.StoreResolver;
import com.jangburich.domain.entity.OrderStatus;
import com.jangburich.domain.entity.Orders;
import com.jangburich.domain.entity.Store;
import com.jangburich.domain.owner.domain.repository.OwnerRepository;
import com.jangburich.domain.repository.OrdersRepository;
import com.jangburich.domain.repository.StoreRepository;
import com.jangburich.domain.store.exception.OrdersNotFoundException;
import com.jangburich.global.payload.ErrorCode;
import com.jangburich.presentation.store.dtos.response.order.OrderDetailResponse;
import com.jangburich.presentation.store.dtos.response.order.OrderGetResponse;
import com.jangburich.presentation.store.dtos.response.store.view.StoreHomeResponse;

import lombok.RequiredArgsConstructor;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class StoreOrderQueryService {

    private final StoreRepository storeRepository;
    private final OwnerRepository ownerRepository;
    private final OrdersRepository ordersRepository;

    private final StoreResolver storeResolver;
    /**
     * 오늘자 주문 내역을 보여준다
     * @param userId : Authentication ID
     * @return OrderTodayResponse - 오늘자 주문 내역 DTO
     */
    public StoreHomeResponse.TodayOrder getTodayOrders(String userId) {
        List<OrderGetResponse> orderGetResponse = new ArrayList<>();

        Store store = storeResolver.getStoreByUserId(userId);

        LocalDateTime startOfDay = LocalDate.now().atStartOfDay(); // 오늘 시작
        LocalDateTime endOfDay = LocalDate.now().plusDays(1).atStartOfDay(); // 내일 시작 (오늘의 끝)

        List<Orders> allByStore = getTodayOrders(store, startOfDay, endOfDay);

        for (Orders orders : allByStore) {
            OrderGetResponse newOrderGetResponse = OrderGetResponse.builder()
                .id(orders.getId())
                .name(orders.getUser().getName())
                .orderStatus(orders.getOrderStatus())
                .teamName(orders.getTeam().getName())
                .price(orders.getOrderPrice())
                .build();

            orderGetResponse.add(newOrderGetResponse);
        }

        return StoreHomeResponse.TodayOrder.of(orderGetResponse);
    }

    /**
     * 지난 주문을 조회한다.
     * @param userId userId
     * @return LastOrder - 지난 주문 내역
     */
    public StoreHomeResponse.LastOrder getOrdersLast(String userId) {
        List<OrderGetResponse> orderGetResponseList = new ArrayList<>();

        Store store = storeResolver.getStoreByUserId(userId);

        LocalDateTime todayStart = LocalDate.now().atStartOfDay();
        List<Orders> lastOrders = getLastOrders(store, todayStart);

        for (Orders orders : lastOrders) {
            orderGetResponseList.add(OrderGetResponse.builder()
                .id(orders.getId())
                .orderStatus(orders.getOrderStatus())
                .name(orders.getUser().getName())
                .teamName(orders.getTeam().getName())
                .price(orders.getOrderPrice())
                .build());
        }

        return StoreHomeResponse.LastOrder.of(orderGetResponseList);
    }

    /**
     * 주문 상세 조회
     * @param userId Authentication ID
     * @param orderId 주문 번호
     * @return
     */
    public OrderDetailResponse getOrderDetails(String userId, Long orderId) {

        Store store = storeResolver.getStoreByUserId(userId);
        Orders orders = ordersRepository.findById(orderId)
            .orElseThrow(() -> new OrdersNotFoundException(ErrorCode.ORDER_NOT_FOUND));


        // 사용 상태
        // 주문 id
        // 그룹 이름
        // 사용자 이름
        // 날짜, 시간
        // 상품 금액
        // 합계 금액

        return null;
/*        return OrderDetailResponse.builder()
            .id(orders.getId())
            .teamName(orders.getTeam().getName())
            .teamUserName(orders.getUser().getName())
            .dateTime(orders.getUpdatedAt())
            .amount(0) // TODO 수정 필요
            .totalPrice(0) // TODO 수정 필요
            .discountPrice(0) // TODO 수정 필요
            .build();*/
    }

    private List<Orders> getLastOrders (Store store, LocalDateTime todayStart) {
        return ordersRepository.findOrdersByStoreAndDateAndStatusNative(store.getId(), todayStart,
            OrderStatus.PaymentStatus());
    }


    private List<Orders> getTodayOrders (Store store, LocalDateTime startOfDay,
        LocalDateTime endOfDay) {
        return ordersRepository.findOrdersByStoreAndTodayDateAndStatus(store.getId(), startOfDay,
            endOfDay, OrderStatus.PaymentStatus());
    }
}
