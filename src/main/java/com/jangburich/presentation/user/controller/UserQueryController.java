package com.jangburich.presentation.user.controller;

import com.jangburich.application.user.service.UserQueryService;
import com.jangburich.global.payload.BaseResponse;
import com.jangburich.global.payload.CommonApiResponse;
import com.jangburich.presentation.user.dto.response.StoreListResponse;
import com.jangburich.utils.parser.AuthenticationParser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@Tag(name = "User", description = "User Query Api")
public class UserQueryController {

    private final UserQueryService userQueryService;

    @GetMapping("/store")
    @Operation(summary = "제휴 매장 조회", description = "유저의 제휴 매장을 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK",
                            content = @Content(schema = @Schema(implementation = BaseResponse.class)))
            })
    @CommonApiResponse
    public ResponseEntity<BaseResponse<?>> getStoreList(Authentication authentication
            , @RequestParam boolean liked
            , @PageableDefault(page = 0, size = 3) Pageable pageable) {
        StoreListResponse storeListResponse = userQueryService.getStoreList(AuthenticationParser.parseUserId(authentication), liked, pageable);

        return ResponseEntity.ok(new BaseResponse<>(storeListResponse, LocalDateTime.now(ZoneId.of("Asia/Seoul")),"OK"));
    }
}
