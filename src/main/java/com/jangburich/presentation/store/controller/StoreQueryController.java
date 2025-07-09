package com.jangburich.presentation.store.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jangburich.presentation.store.dtos.response.order.OrderDetailResponse;
import com.jangburich.presentation.store.dtos.response.order.OrderGetResponse;
import com.jangburich.presentation.store.dtos.response.payment.PaymentGroupDetailResponse;
import com.jangburich.presentation.store.dtos.response.store.StoreSearchDetailsResponse;
import com.jangburich.presentation.store.dtos.response.store.view.StoreHomeResponse;
import com.jangburich.application.store.service.StoreQueryService;
import com.jangburich.global.payload.ResponseCustom;
import com.jangburich.utils.parser.AuthenticationParser;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Store", description = "Store API")
@RequiredArgsConstructor
@RequestMapping("/api/v1/store")
@RestController
public class StoreQueryController {
    private final StoreQueryService storeQueryService;

    @Operation(summary = "가게 전용 코드 조회", description = "가게 전용 코드를 조회한다.")
    @GetMapping("/unique-code")
    public ResponseCustom<StoreHomeResponse.UniqueCode> getStoreUniqueCode(Authentication authentication) {
        return ResponseCustom.OK(storeQueryService.getStoreUniqueCode(AuthenticationParser.parseUserId(authentication)));
    }

    @Operation(summary = "나의 장부 조회", description = "오늘 기준 나의 장부를 조회한다.")
    @GetMapping
    public ResponseCustom<StoreHomeResponse.AccountInfo> getStoreInfo(Authentication authentication) {
        return ResponseCustom.OK(storeQueryService.getStoreAccountInfo(AuthenticationParser.parseUserId(authentication)));
    }

    @Operation(summary = "결제 그룹 조회", description = "장부 결제 그룹을 조회합니다.")
    @GetMapping("/payment-group")
    public ResponseCustom<List<StoreHomeResponse.TodayPaymentTeam>> getPaymentGroup(Authentication authentication) {
        return ResponseCustom.OK(
            storeQueryService.getPaymentGroup(AuthenticationParser.parseUserId(authentication)));
    }

    @Operation(summary = "오늘 주문 조회", description = "가게에 있는 오늘 주문을 조회합니다")
    @GetMapping("/orders/today")
    public ResponseCustom<StoreHomeResponse.TodayOrder> getTodayOrders(Authentication authentication) {
        return ResponseCustom.OK(storeQueryService.getTodayOrders(
            AuthenticationParser.parseUserId(authentication)));
    }

    @Operation(summary = "매장 상세 페이지 조회", description = "매장을 상세 조회합니다.")
    @GetMapping("/{storeId}")
    public ResponseCustom<StoreSearchDetailsResponse> storeSearchDetails(
        Authentication authentication,
        @PathVariable Long storeId
    ) {
        // TODO API 수정 필요
        return ResponseCustom.OK();
    }

    @Operation(summary = "결제 그룹 상세 조회", description = "장부 결제 그룹을 상세 조회합니다.")
    @GetMapping("/payment-group/{teamId}")
    public ResponseCustom<PaymentGroupDetailResponse> getPaymentGroupDetail(Authentication authentication,
        @PathVariable Long teamId) {
        return ResponseCustom.OK(
            storeQueryService.getPaymentGroupDetail(AuthenticationParser.parseUserId(authentication), teamId));
    }

    @Operation(summary = "결제 내역 조회", description = "가게에서 일어난 결제 내역을 조회합니다.")
    @GetMapping("/payment-history")
    public ResponseCustom<?> getPaymentHistory(Authentication authentication) {
        return ResponseCustom.OK(
            storeQueryService.getPaymentHistory(AuthenticationParser.parseUserId(authentication)));
    }

    @Operation(summary = "지난 주문 조회", description = "가게에 있는 지난 주문을 조회합니다")
    @GetMapping("/orders/last")
    public ResponseCustom<List<OrderGetResponse>> getLastOrders(Authentication authentication) {
        List<OrderGetResponse> ordersLast = storeQueryService.getOrdersLast(
            AuthenticationParser.parseUserId(authentication));
        return ResponseCustom.OK(ordersLast);
    }

    @Operation(summary = "주문 상세 조회", description = "가게에 있는 주문을 상세 조회합니다")
    @GetMapping("/orders/{ordersId}")
    public ResponseCustom<OrderDetailResponse> getOrders(Authentication authentication, @RequestParam Long orderId) {
        return ResponseCustom.OK(
            storeQueryService.getOrderDetails(AuthenticationParser.parseUserId(authentication), orderId));
    }
}
