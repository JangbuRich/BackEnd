package com.jangburich.presentation.store.controller.query;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jangburich.application.store.service.query.StorePrepayTeamQueryService;
import com.jangburich.global.payload.BaseResponse;
import com.jangburich.presentation.store.dtos.response.store.StorePrepayTeamResponse;
import com.jangburich.utils.parser.AuthenticationParser;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Store", description = "Store Prepay Team Query(조회) API")
@RequiredArgsConstructor
@RequestMapping("/api/v1/store-prepay")
@RestController
public class StorePrepayTeamQueryController {
    private final StorePrepayTeamQueryService storePrepayTeamQueryService;

    @Operation(summary = "선결제 신청한 그룹 조회", description = "해당 가게에 선결제 신청한 그룹을 조회한다.")
    @GetMapping
    public ResponseEntity<?> getStorePrepayApplicationTeam (Authentication authentication) {
        List<StorePrepayTeamResponse.teamInfo> storePrepayApplicationTeam = storePrepayTeamQueryService.getStorePrepayApplicationTeam(
            AuthenticationParser.parseUserId(authentication));

        return ResponseEntity.ok(new BaseResponse<>(storePrepayApplicationTeam, LocalDateTime.now(), "OK"));
    }

    @Operation(summary = "선결제 신청한 그룹 상세 조회", description = "해당 가게에 선결제 신청한 그룹 승인을 위한 상세 조회를 진행한다.")
    @GetMapping("/{teamId}")
    public ResponseEntity<?> getStorePrepayApplicationTeamDetail (Authentication authentication,
        @PathVariable Long teamId) {
        StorePrepayTeamResponse.ApprovalDetail storePrepayApplicationTeamDetail = storePrepayTeamQueryService.getStorePrepayApplicationTeamDetail(
            AuthenticationParser.parseUserId(authentication), teamId);

        return ResponseEntity.ok(new BaseResponse<>(storePrepayApplicationTeamDetail, LocalDateTime.now(), "OK"));
    }

}
