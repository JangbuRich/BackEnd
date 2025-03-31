package com.jangburich.domain.order.presentation;

import com.jangburich.domain.order.application.OrderService;
import com.jangburich.domain.order.dto.request.AddCartRequest;
import com.jangburich.domain.order.dto.request.OrderRequest;
import com.jangburich.domain.order.dto.response.CartResponse;
import com.jangburich.domain.order.dto.response.OrderResponse;
import com.jangburich.global.payload.Message;
import com.jangburich.global.payload.ResponseCustom;
import com.jangburich.utils.parser.AuthenticationParser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Order", description = "Order API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    @Operation(summary = "장바구니 담기", description = "장바구니에 물건과 수량을 저장합니다.")
    @PostMapping("/carts")
    public ResponseCustom<Message> addCart(
            Authentication authentication,
            @RequestBody AddCartRequest addCartRequest
    ) {
        return ResponseCustom.OK(orderService.addCart(AuthenticationParser.parseUserId(authentication), addCartRequest));
    }

    @Operation(summary = "장바구니 비우기", description = "장바구니를 완전히 비웁니다.")
    @DeleteMapping
    public ResponseCustom<Message> removeCart(
            Authentication authentication
    ) {
        return ResponseCustom.OK(orderService.removeCart(AuthenticationParser.parseUserId(authentication)));
    }

    @Operation(summary = "장바구니 조회", description = "장바구니에 담은 상품을 조회합니다.")
    @GetMapping("/carts")
    public ResponseCustom<CartResponse> getCartItems(
            Authentication authentication
    ) {
        return ResponseCustom.OK(orderService.getCartItems(AuthenticationParser.parseUserId(authentication)));
    }

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
