package com.jangburich.domain.order.presentation;

import com.jangburich.domain.order.application.OrderService;
import com.jangburich.domain.order.dto.request.OrderRequest;
import com.jangburich.domain.order.dto.response.OrderResponse;
import com.jangburich.global.payload.Message;
import com.jangburich.global.payload.ResponseCustom;
import com.jangburich.utils.parser.AuthenticationParser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Order", description = "Order API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    @Operation(summary = "상품 주문", description = "상품을 주문합니다.")
    @PostMapping
    public ResponseCustom<OrderResponse> order(
            Authentication authentication,
            @RequestBody OrderRequest orderRequest
    ) {
        return ResponseCustom.OK(orderService.order(AuthenticationParser.parseUserId(authentication), orderRequest));
    }

    @Operation(summary = "식권 사용", description = "식권을 사용합니다.")
    @PostMapping("/tickets/{orderId}")
    public ResponseCustom<Message> useMealTicket(
            Authentication authentication,
            @PathVariable Long orderId
    ) {
        return ResponseCustom.OK(orderService.useMealTicket(AuthenticationParser.parseUserId(authentication), orderId));
    }
}
