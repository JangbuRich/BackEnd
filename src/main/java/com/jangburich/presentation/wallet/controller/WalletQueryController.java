package com.jangburich.presentation.wallet.controller;

import com.jangburich.application.wallet.service.WalletQueryService;
import com.jangburich.global.payload.BaseResponse;
import com.jangburich.global.payload.CommonApiResponse;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.ZoneId;

@RestController
@RequestMapping("/api/v1/wallet")
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Tag(name = "Wallet", description = "Wallet Query Api")
public class WalletQueryController {

    private final WalletQueryService walletQueryService;

    @GetMapping()
    @Operation(summary = "내 장부 조회", description = "현재 남아 있는 포인트를 조회합니다.",
        responses = {
            @ApiResponse(responseCode = "201", description = "생성 성공",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @CommonApiResponse
    public ResponseEntity<BaseResponse<?>> getMyWallet(Authentication authentication
            , @PageableDefault(page = 0, size = 3, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        WalletResponse walletResponse = walletQueryService.getMyWallet(AuthenticationParser.parseUserId(authentication), pageable);

        return ResponseEntity.ok(new BaseResponse<>(walletResponse, LocalDateTime.now(ZoneId.of("Asia/Seoul")),"OK"));
    }
}
