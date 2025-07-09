package com.jangburich.presentation.prepay.controller;

import com.jangburich.application.service.prepay.PrepayQueryService;
import com.jangburich.global.payload.ResponseCustom;
import com.jangburich.utils.parser.AuthenticationParser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Prepay", description = "Prepay API")
@RestController
@RequiredArgsConstructor
public class PrepayQueryController {

    private final PrepayQueryService prepayQueryService;

    @Operation(summary = "선결제 정보 조회", description = "선결제 진행하기 위한 정보를 조회합니다.")
    @GetMapping("")
    public ResponseCustom<?> getPrepayInfo(Authentication authentication,
                                           @RequestParam(value = "storeId") Long storeId,
                                           @RequestParam(value = "teamId") Long teamId
    ) {
        return ResponseCustom.OK(
                prepayQueryService.getPrepayInfo(AuthenticationParser.parseUserId(authentication), storeId, teamId));
    }
}
