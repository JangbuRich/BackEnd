package com.jangburich.presentation.store.controller.query;

import com.jangburich.global.payload.BaseResponse;
import com.jangburich.presentation.store.dtos.response.store.StoreInfoResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jangburich.presentation.store.dtos.response.store.view.StoreHomeResponse;
import com.jangburich.application.store.service.query.StoreQueryService;
import com.jangburich.global.payload.ResponseCustom;
import com.jangburich.utils.parser.AuthenticationParser;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Tag(name = "Store", description = "Store Query(조회) API")
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

    @Operation(summary = "매장 정보 조회", description = "[매장 정보 관리 Tab] 매장 전체 정보를 조회한다.")
    @GetMapping("/store/info")
    public ResponseEntity<?> getStorePrepayDetail(Authentication authentication) {
        StoreInfoResponse storeInfo = storeQueryService.getStoreInfo(AuthenticationParser.parseUserId(authentication));

        return ResponseEntity.ok(new BaseResponse<>(storeInfo, LocalDateTime.now(), "OK"));
    }

    // Todo: 패키지에 맞게 api 옮기기
    @Operation(summary = "결제 내역 조회", description = "가게에서 일어난 결제 내역을 조회합니다.")
    @GetMapping("/payment-history")
    public ResponseCustom<?> getPaymentHistory(Authentication authentication) {
        return ResponseCustom.OK(
            storeQueryService.getPaymentHistory(AuthenticationParser.parseUserId(authentication)));
    }

}
