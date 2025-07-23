package com.jangburich.presentation.team.controller;

import java.util.List;

import com.jangburich.presentation.team.dto.request.*;
import com.jangburich.presentation.team.dto.response.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jangburich.application.team.service.TeamService;
import com.jangburich.global.payload.Message;
import com.jangburich.global.payload.ResponseCustom;
import com.jangburich.utils.parser.AuthenticationParser;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Team", description = "Team API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/teams")
public class TeamController {

    private final TeamService teamService;

    @Operation(summary = "팀 생성", description = "팀을 생성한다. 팀 리더는 생성자")
    @PostMapping
    public ResponseCustom<TeamSecretCodeResponse> registerTeam(Authentication authentication, @RequestBody RegisterTeamRequest registerTeamRequest) {
        return ResponseCustom.OK(teamService.registerTeam(AuthenticationParser.parseUserId(authentication), registerTeamRequest));
    }

    @Operation(summary = "팀 가입", description = "비밀 코드를 입력해 팀에 가입한다.")
    @PostMapping("/join/{joinCode}")
    public ResponseCustom<Message> joinTeam(Authentication authentication, @PathVariable("joinCode") String joinCode) {
        return ResponseCustom.OK(teamService.joinTeam(AuthenticationParser.parseUserId(authentication), joinCode));
    }

    @Operation(summary = "그룹 유형 조회", description = "그룹 유형을 조회한다.")
    @GetMapping("/categories")
    public ResponseCustom<?> getCategories() {
        // TODO 개발 예정
        return null;
    }

    @Operation(summary = "해당 그룹의 환불 가능 금액 조회", description = "해당 그룹의 환불 가능 금액을 조회합니다.")
    @GetMapping("/{teamId}/available-refund-amount")
    public ResponseCustom<?> getAvailableRefundAmount(Authentication authentication, @PathVariable Long teamId) {
        // TODO 그룹 삭제할 때 보여지는 환불 가능 금액 조회
        return null;
    }

    @Operation(summary = "그룹(팀) 비밀코드 조회", description = "비밀코드를 입력하면, 그 팀을 조회하는 api 입니다.")
    @GetMapping("/info/secretcode/{secretCode}")
    public ResponseCustom<TeamCodeResponse> getTeamWithSecretCode(@PathVariable String secretCode) {
        return ResponseCustom.OK(teamService.getTeamsWithSecretCode(secretCode));
    }
}