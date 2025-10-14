package com.jangburich.presentation.wallet.controller;

import com.jangburich.application.wallet.service.WalletQueryService;
import com.jangburich.global.payload.BaseResponse;
import com.jangburich.global.payload.CommonApiResponse;
import com.jangburich.presentation.wallet.dto.response.PointTransactionList;
import com.jangburich.presentation.wallet.dto.response.PointTransactionItem;
import com.jangburich.presentation.wallet.dto.response.WalletResponse;
import com.jangburich.utils.parser.AuthenticationParser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

@RestController
@RequestMapping("/api/v1/wallet")
@RequiredArgsConstructor
@Tag(name = "Wallet", description = "Wallet Query Api")
public class WalletQueryController {

    private final WalletQueryService walletQueryService;

    @GetMapping()
    @Operation(summary = "내 장부 조회", description = "현재 남아 있는 포인트를 조회합니다.", responses = {@ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = BaseResponse.class)))})
    @CommonApiResponse
    public ResponseEntity<BaseResponse<?>> getMyWallet(Authentication authentication, @PageableDefault(page = 0, size = 3, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        WalletResponse walletResponse = walletQueryService.getMyWallet(AuthenticationParser.parseUserId(authentication), pageable);

        return ResponseEntity.ok(new BaseResponse<>(walletResponse, LocalDateTime.now(ZoneId.of("Asia/Seoul")), "OK"));
    }

    @GetMapping("/point")
    @Operation(summary = "포인트 결제 내역 조회", description = "선결제 / 금액사용 각 리스트 조회", responses = {@ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = BaseResponse.class)))})
    @CommonApiResponse
    public ResponseEntity<BaseResponse<?>> getPoint(Authentication authentication, @RequestParam(required = true) Boolean prePay, @RequestParam(required = false) LocalDate createdAfter, @RequestParam(required = false) LocalDate createdBefore, @PageableDefault(page = 0, size = 3, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        PointTransactionList pointResponse = walletQueryService.getPointList(AuthenticationParser.parseUserId(authentication), prePay, createdAfter, createdBefore, pageable);

        return ResponseEntity.ok(new BaseResponse<>(pointResponse, LocalDateTime.now(ZoneId.of("Asia/Seoul")), "OK"));
    }

    @GetMapping("/point/{transactionId}")
    @Operation(summary = "결제 내역 상세 조회", description = "선결제 / 포인트 차감결제 각 상세 조회", responses = {@ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = BaseResponse.class)))})
    @CommonApiResponse
    public ResponseEntity<BaseResponse<?>> getPointDetail(Authentication authentication, @PathVariable long transactionId, @RequestParam(required = true) Boolean prePay) {
        PointTransactionItem pointTransactionItem = walletQueryService.getPointDetail(AuthenticationParser.parseUserId(authentication), transactionId, prePay);

        return ResponseEntity.ok(new BaseResponse<>(pointTransactionItem, LocalDateTime.now(ZoneId.of("Asia/Seoul")), "OK"));
    }

}
