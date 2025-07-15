package com.jangburich.presentation.store.controller.query;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
@RestController
public class StoreTeamQueryController {
    private final StoreTeamQueryService storeTeamQueryService;

    @Operation(summary = "결제 그룹 조회", description = "장부 결제 그룹을 조회합니다.")
    @GetMapping("/payment-group")
    public ResponseCustom<List<StoreHomeResponse.TodayPaymentTeam>> getPaymentGroup(Authentication authentication) {
        return ResponseCustom.OK(
            storeTeamQueryService.getPaymentGroup(AuthenticationParser.parseUserId(authentication)));
    }

    @Operation(summary = "결제 그룹 상세 조회", description = "장부 결제 그룹을 상세 조회합니다.")
    @GetMapping("/payment-group/{teamId}")
    public ResponseCustom<PaymentGroupDetailResponse> getPaymentGroupDetail(Authentication authentication,
        @PathVariable Long teamId) {
        return ResponseCustom.OK(
            storeTeamQueryService.getPaymentGroupDetail(AuthenticationParser.parseUserId(authentication), teamId));
    }
}
