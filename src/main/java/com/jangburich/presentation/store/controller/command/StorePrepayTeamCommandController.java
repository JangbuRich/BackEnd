package com.jangburich.presentation.store.controller.command;

import java.time.LocalDateTime;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jangburich.application.store.service.command.StorePrepayTeamCommandService;
import com.jangburich.global.payload.BaseResponse;
import com.jangburich.presentation.store.dtos.request.StorePrepayTeamRequest;
import com.jangburich.presentation.store.dtos.response.store.StorePrepayTeamResponse;
import com.jangburich.utils.parser.AuthenticationParser;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Store", description = "Store Prepay Team Command API")
@RequiredArgsConstructor
@RequestMapping("/api/v1/store-prepay")
@RestController
public class StorePrepayTeamCommandController {
    private final StorePrepayTeamCommandService storePrepayTeamCommandService;

    @Operation(summary = "선결제 신청 그룹 승인", description = "해당 가게에 선결제 신청한 그룹을 승인한다.")
    @PostMapping("/approval")
    public ResponseEntity<?> storePrepayApproval(Authentication authentication, @Valid @RequestBody StorePrepayTeamRequest.ApprovalInfo approvalInfo) {
        StorePrepayTeamResponse.approvalResponse approvalResponse = storePrepayTeamCommandService.approvePrepayTeam(
            AuthenticationParser.parseUserId(authentication), approvalInfo.getTeamId());

        return ResponseEntity.ok(new BaseResponse<>(approvalResponse, LocalDateTime.now(), "OK"));
    }
}
