package com.jangburich.presentation.prepay.controller;

import com.jangburich.application.service.prepay.PrepayCommandService;
import com.jangburich.global.payload.Message;
import com.jangburich.global.payload.ResponseCustom;
import com.jangburich.presentation.prepay.dto.request.PrepayRequest;
import com.jangburich.utils.parser.AuthenticationParser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Tag(name="Prepay",description = "Prepay Command Api")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/prepay")
public class PrepayCommandController {

    private final PrepayCommandService prepayCommandService;

    @Operation(summary = "선결제", description = "팀과 매장 선결제를 진행합니다.")
    @PostMapping
    public ResponseCustom<Message> prepay(Authentication authentication,
                                          @RequestBody PrepayRequest prepayRequest) {
        return ResponseCustom.OK(prepayCommandService.prepay(AuthenticationParser.parseUserId(authentication), prepayRequest));
    }
}
