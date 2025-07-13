package com.jangburich.presentation.store.controller;

import java.time.LocalDateTime;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jangburich.application.store.service.StoreOrderQueryService;
import com.jangburich.global.payload.BaseResponse;
import com.jangburich.global.payload.ResponseCustom;
import com.jangburich.presentation.store.dtos.response.order.OrderDetailResponse;
import com.jangburich.presentation.store.dtos.response.store.view.StoreHomeResponse;
import com.jangburich.utils.parser.AuthenticationParser;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Store Order Query", description = "사장님 주문 조회 API")
@RequestMapping("/api/v1/store")
@RequiredArgsConstructor
@RestController
public class StoreOrderQueryController {
    private final StoreOrderQueryService storeOrderQueryService;

    @Operation(summary = "오늘 주문 조회", description = "가게에 있는 오늘 주문을 조회합니다")
    @GetMapping("/orders/today")
    public ResponseCustom<StoreHomeResponse.TodayOrder> getTodayOrders(Authentication authentication) {
        return ResponseCustom.OK(storeOrderQueryService.getTodayOrders(
            AuthenticationParser.parseUserId(authentication)));
    }

    @Operation(summary = "지난 주문 조회", description = "가게에 있는 지난 주문을 조회합니다")
    @GetMapping("/orders/last")
    public ResponseEntity<BaseResponse<StoreHomeResponse.LastOrder>> getLastOrders(Authentication authentication) {
        StoreHomeResponse.LastOrder ordersLast = storeOrderQueryService.getOrdersLast(
            AuthenticationParser.parseUserId(authentication));

        return ResponseEntity.ok(new BaseResponse<>(ordersLast, LocalDateTime.now(), "OK"));
    }

    @Operation(summary = "주문 상세 조회", description = "가게에 있는 주문을 상세 조회합니다")
    @GetMapping("/orders/{orderId}")
    public ResponseEntity<BaseResponse<OrderDetailResponse>> getOrderDetail(Authentication authentication, @RequestParam Long orderId) {
        OrderDetailResponse orderDetails = storeOrderQueryService.getOrderDetails(
            AuthenticationParser.parseUserId(authentication), orderId);

        return ResponseEntity.ok(new BaseResponse<>(orderDetails, LocalDateTime.now(), "OK"));
    }

}
