package com.jangburich.presentation.prepay.controller.query;

import java.time.LocalDateTime;
import java.util.List;

import com.jangburich.application.prepay.service.PrepayQueryService;
import com.jangburich.global.payload.BaseResponse;
import com.jangburich.global.payload.ResponseCustom;
import com.jangburich.presentation.prepay.dto.response.PrepayResponse;
import com.jangburich.utils.parser.AuthenticationParser;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Prepay", description = "Prepay Query API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/prepay")
public class PrepayQueryController {

    private final PrepayQueryService prepayQueryService;

    @Operation(summary = "선결제 정보 조회", description = "선결제 진행하기 위한 정보를 조회합니다.")
    @GetMapping
    public ResponseEntity<?> getPrepayInfo(Authentication authentication, @RequestParam(value = "storeId") Long storeId, @RequestParam(value = "teamId") Long teamId) {
        return ResponseEntity.ok(new BaseResponse<>(prepayQueryService.getPrepayInfo(AuthenticationParser.parseUserId(authentication), storeId, teamId), LocalDateTime.now(), "OK"));
    }

    @Operation(summary = "가게 선결제 내역 조회", description = "해당 가게에 선결제된 결제 정보를 조회한다.")
    @GetMapping("/store")
    public ResponseEntity<?> getStorePrepay(Authentication authentication) {
        List<PrepayResponse.StorePrepayInfo> storePrepayInfo = prepayQueryService.getStorePrepayInfo(AuthenticationParser.parseUserId(authentication));

        return ResponseEntity.ok(new BaseResponse<>(storePrepayInfo, LocalDateTime.now(), "OK"));
    }

}
