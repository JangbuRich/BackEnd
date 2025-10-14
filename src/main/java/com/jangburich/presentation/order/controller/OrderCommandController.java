package com.jangburich.presentation.order.controller;

import com.jangburich.application.order.service.OrderCommandService;
import com.jangburich.global.payload.BaseResponse;
import com.jangburich.global.payload.CommonApiResponse;
import com.jangburich.presentation.order.dto.request.OrderRequest;
import com.jangburich.presentation.order.dto.request.UseTicketRequest;
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
    @Operation(summary = "Issue Voucher", description = "Issue a voucher", responses = {@ApiResponse(responseCode = "201", description = "생성 성공", content = @Content(schema = @Schema(implementation = BaseResponse.class)))})
    public ResponseEntity<Void> order(Authentication authentication, @RequestBody OrderRequest orderRequest) {
        long orderId = orderCommandService.order(AuthenticationParser.parseUserId(authentication), orderRequest);

        URI location = URI.create("/order/" + orderId);

        return ResponseEntity.created(location).build();
    }

    @PostMapping("/{orderId}")
    @CommonApiResponse
    @Operation(summary = "User Voucher", description = "Use issued voucher", responses = {@ApiResponse(responseCode = "204", description = "No Content", content = @Content(schema = @Schema(implementation = BaseResponse.class)))})
    public ResponseEntity<Void> useMealTicket(Authentication authentication, @PathVariable Long orderId, @RequestBody UseTicketRequest useTicketRequest) {
        orderCommandService.useTicket(AuthenticationParser.parseUserId(authentication), orderId, useTicketRequest);

        return ResponseEntity.noContent().build();
    }
}
