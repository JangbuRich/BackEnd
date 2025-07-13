package com.jangburich.presentation.order.controller;

import com.jangburich.application.order.service.OrderQueryService;
import com.jangburich.global.payload.BaseResponse;
import com.jangburich.global.payload.CommonApiResponse;
import com.jangburich.presentation.order.dto.response.OrderResponse;
import com.jangburich.utils.parser.AuthenticationParser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Tag(name = "Order", description = "Order Query Api")
@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
public class OrderQueryController {

    private final OrderQueryService orderQueryService;

    @GetMapping("/{orderId}")
    @CommonApiResponse
    @Operation(summary = "Get Voucher", description = "Get voucher detail info"
            , responses = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    public ResponseEntity<BaseResponse<?>> getOrder(
            Authentication authentication
            , @PathVariable long orderId
    ) {
        OrderResponse orderResponse = orderQueryService.getOrder(AuthenticationParser.parseUserId(authentication), orderId);
        return ResponseEntity.ok(new BaseResponse<>(orderResponse, LocalDateTime.now(ZoneId.of("Asia/Seoul")),"OK" ));
    }
}
