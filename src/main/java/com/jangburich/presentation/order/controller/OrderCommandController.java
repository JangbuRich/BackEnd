package com.jangburich.presentation.order.controller;

import com.jangburich.application.service.order.OrderCommandService;
import com.jangburich.global.payload.BaseResponse;
import com.jangburich.global.payload.CommonApiResponse;
import com.jangburich.global.payload.Message;
import com.jangburich.global.payload.ResponseCustom;
import com.jangburich.presentation.order.dto.request.OrderRequest;
import com.jangburich.utils.parser.AuthenticationParser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@Tag(name = "Order", description = "Order Command Api")
@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
public class OrderCommandController {

    private final OrderCommandService orderCommandService;

    @PostMapping
    @CommonApiResponse
    @Operation(summary = "Issue Voucher", description = "Issue a voucher"
    , responses = {
            @ApiResponse(responseCode = "201", description = "생성 성공",
            content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    public ResponseEntity<Void> order(
            Authentication authentication,
            @RequestBody OrderRequest orderRequest
    ) {
        long orderId= orderCommandService.order(AuthenticationParser.parseUserId(authentication), orderRequest);

        URI location= URI.create("/order/"+orderId);

        return ResponseEntity.created(location).build();
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
