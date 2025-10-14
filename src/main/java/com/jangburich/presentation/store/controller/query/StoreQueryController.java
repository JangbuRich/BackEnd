package com.jangburich.presentation.store.controller.query;

import com.jangburich.domain.entity.Category;
import com.jangburich.global.payload.BaseResponse;
import com.jangburich.global.payload.CommonApiResponse;
import com.jangburich.presentation.store.dtos.response.store.StoreDetailsResponse;
import com.jangburich.presentation.store.dtos.response.store.StoreListResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import com.jangburich.presentation.store.dtos.response.store.StoreInfoResponse;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
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
import java.time.ZoneId;

@Tag(name = "Store", description = "Store Query(조회) API")
@RequiredArgsConstructor
@RequestMapping("/api/v1/store")
@RestController
public class StoreQueryController {
    private final StoreQueryService storeQueryService;

    @Operation(summary = "가게 전용 코드 조회", description = "가게 전용 코드를 조회한다.")
    @GetMapping("/unique-code")
    public ResponseEntity<?> getStoreUniqueCode(Authentication authentication) {
        return ResponseEntity.ok(new BaseResponse<>(storeQueryService.getStoreUniqueCode(AuthenticationParser.parseUserId(authentication)), LocalDateTime.now(), "OK"));
    }

    @Operation(summary = "나의 장부 조회", description = "오늘 기준 나의 장부를 조회한다.")
    @GetMapping
    public ResponseEntity<?> getStoreInfo(Authentication authentication) {
        return ResponseEntity.ok(new BaseResponse<>(storeQueryService.getStoreAccountInfo(AuthenticationParser.parseUserId(authentication)), LocalDateTime.now(), "OK"));
    }

    @GetMapping("/category")
    @Operation(summary = "카테고리 별 가게 목록 조회", description = "카테고리 별로 가게 목록을 조회합니다.", responses = {@ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = BaseResponse.class)))})
    @CommonApiResponse
    public ResponseEntity<BaseResponse<?>> searchByCategory(Authentication authentication, @RequestParam(required = false, defaultValue = "3") Integer searchRadius, @RequestParam(required = false, defaultValue = "전체") String category, Double lat, Double lon, Pageable pageable) {
        Category categoryEnum = Category.fromDisplayName(category);
        StoreListResponse storeListResponse = storeQueryService.getStoreListByCategory(AuthenticationParser.parseUserId(authentication), searchRadius, categoryEnum, lat, lon, pageable);

        return ResponseEntity.ok(new BaseResponse<>(storeListResponse, LocalDateTime.now(ZoneId.of("Asia/Seoul")), "OK"));
    }

    @GetMapping("/search")
    @Operation(summary = "매장 찾기(검색)", description = "검색어와 매장 유형에 맞는 매장을 검색합니다.", responses = {@ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = BaseResponse.class)))})
    @CommonApiResponse
    public ResponseEntity<BaseResponse<?>> searchStores(Authentication authentication, @RequestParam(required = false, defaultValue = "") String keyword, Pageable pageable) {
        StoreListResponse storeListResponse = storeQueryService.searchStores(AuthenticationParser.parseUserId(authentication), keyword, pageable);

        return ResponseEntity.ok(new BaseResponse<>(storeListResponse, LocalDateTime.now(ZoneId.of("Asia/Seoul")), "OK"));
    }

    @GetMapping("/{storeId}")
    @Operation(summary = "매장 상세 페이지 조회", description = "매장을 상세 조회합니다.", responses = {@ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = BaseResponse.class)))})
    @CommonApiResponse
    public ResponseEntity<BaseResponse<?>> storeSearchDetails(Authentication authentication, @PathVariable Long storeId) {
        StoreDetailsResponse storeDetailsResponse = storeQueryService.getStoreDetail(AuthenticationParser.parseUserId(authentication), storeId);

        return ResponseEntity.ok(new BaseResponse<>(storeDetailsResponse, LocalDateTime.now(ZoneId.of("Asia/Seoul")), "OK"));
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
        return ResponseCustom.OK(storeQueryService.getPaymentHistory(AuthenticationParser.parseUserId(authentication)));
    }

}
