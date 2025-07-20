package com.jangburich.presentation.store.controller.query;

import java.time.LocalDateTime;
import java.util.List;

import com.jangburich.global.payload.BaseResponse;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.jangburich.application.store.service.query.StoreTeamQueryService;
import com.jangburich.global.payload.ResponseCustom;
import com.jangburich.presentation.store.dtos.response.payment.PaymentGroupDetailResponse;
import com.jangburich.presentation.store.dtos.response.store.view.StoreHomeResponse;
import com.jangburich.utils.parser.AuthenticationParser;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Store", description = "Store Team Query(조회) API")
@RequiredArgsConstructor
@RequestMapping("/api/v1/store-team")
@Validated
@RestController
public class StoreTeamQueryController {
    private final StoreTeamQueryService storeTeamQueryService;

    @Operation(summary = "결제 그룹 조회", description = "장부 결제 그룹을 조회합니다.")
    @GetMapping("/payment-group")
    public ResponseCustom<List<StoreHomeResponse.TodayPaymentTeam>> getPaymentGroup(Authentication authentication) {
        return ResponseCustom.OK(
                storeTeamQueryService.getPaymentGroup(AuthenticationParser.parseUserId(authentication)));
    }

    @Operation(summary = "가게 선결제 내역 조회", description = "해당 가게에 선결제된 결제 정보를 조회한다.(장부관리 -> 전체결제 그룹 조회 후 -> 그룹 클릭시")
    @GetMapping("/store/{teamId}")
    public ResponseEntity<?> getStorePrepayDetail(Authentication authentication,
                                                  @NotNull(message = "teamId must not be empty") @PathVariable Long teamId) {
        PaymentGroupDetailResponse paymentGroupDetail = storeTeamQueryService.getPaymentGroupDetail(AuthenticationParser.parseUserId(authentication), teamId);

        return ResponseEntity.ok(new BaseResponse<>(paymentGroupDetail, LocalDateTime.now(), "OK"));
    }
}
