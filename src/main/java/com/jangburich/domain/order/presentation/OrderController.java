package com.jangburich.domain.order.presentation;

import com.jangburich.domain.order.application.OrderService;
import com.jangburich.domain.order.dto.request.AddCartRequest;
import com.jangburich.domain.order.dto.response.CartResponse;
import com.jangburich.global.payload.Message;
import com.jangburich.global.payload.ResponseCustom;
import com.jangburich.utils.parser.AuthenticationParser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Order", description = "Order API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    @Operation(summary = "장바구니 담기", description = "장바구니에 물건과 수량을 담습니다.")
    @PostMapping("/carts")
    public ResponseCustom<Message> addCart(
            Authentication authentication,
            @RequestBody AddCartRequest addCartRequest
    ) {
        return ResponseCustom.OK(orderService.addCart(AuthenticationParser.parseUserId(authentication), addCartRequest));
    }

    @Operation(summary = "장바구니 조회", description = "장바구니에 담은 상품을 조회합니다.")
    @GetMapping("/carts")
    public ResponseCustom<CartResponse> getCartItems(
            Authentication authentication
    ) {
        return ResponseCustom.OK(orderService.getCartItems(AuthenticationParser.parseUserId(authentication)));
    }
}
