package com.jangburich.presentation.wallet.controller;

import com.jangburich.application.wallet.service.WalletQueryService;
import com.jangburich.global.payload.ResponseCustom;
import com.jangburich.presentation.wallet.dto.response.WalletResponse;
import com.jangburich.utils.parser.AuthenticationParser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Wallet", description = "Wallet Query Api")
@RestController
@RequestMapping("/api/v1/wallet")
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WalletQueryController {

    private final WalletQueryService walletQueryService;

    @Operation(summary = "내 장부 조회", description = "현재 남아 있는 포인트를 조회합니다.")
    @GetMapping("/{userId}")
    public ResponseCustom<WalletResponse> getMyWallet(Authentication authentication) {
        return ResponseCustom.OK(walletQueryService.getMyWallet(AuthenticationParser.parseUserId(authentication)));
    }
}
