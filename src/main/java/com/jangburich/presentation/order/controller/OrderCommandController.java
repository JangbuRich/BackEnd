package com.jangburich.presentation.order.controller;

import com.jangburich.application.service.order.OrderCommandService;
import com.jangburich.global.payload.Message;
import com.jangburich.global.payload.ResponseCustom;
import com.jangburich.presentation.order.dto.request.OrderRequest;
import com.jangburich.presentation.order.dto.response.OrderResponse;
import com.jangburich.utils.parser.AuthenticationParser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Order", description = "Order Command Api")
@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
public class OrderCommandController {

    private final OrderCommandService orderCommandService;

    @Operation(summary = "상품 주문", description = "상품을 주문합니다.")
    @PostMapping
    public ResponseCustom<OrderResponse> order(
            Authentication authentication,
            @RequestBody OrderRequest orderRequest
    ) {
        return ResponseCustom.OK(orderCommandService.order(AuthenticationParser.parseUserId(authentication), orderRequest));
    }

    @Operation(summary = "식권 사용", description = "식권을 사용합니다.")
    @PostMapping("/tickets/{orderId}")
    public ResponseCustom<Message> useMealTicket(
            Authentication authentication,
            @PathVariable Long orderId
    ) {
        return ResponseCustom.OK(orderCommandService.useMealTicket(AuthenticationParser.parseUserId(authentication), orderId));
    }
}
