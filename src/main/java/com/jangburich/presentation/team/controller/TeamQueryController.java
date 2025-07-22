package com.jangburich.presentation.team.controller;

import com.jangburich.application.team.service.TeamQueryService;
import com.jangburich.presentation.team.dto.response.myTeam.MyTeamDetailResponse;
import com.jangburich.presentation.team.dto.response.myTeam.MyTeamResponse;
import com.jangburich.global.payload.BaseResponse;
import com.jangburich.global.payload.CommonApiResponse;
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

@Tag(name = "Team", description = "Team Query API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/teams")
public class TeamQueryController {

    private final TeamQueryService teamQueryService;

    @GetMapping
    @Operation(summary = "내가 속한 그룹 조회", description = "내가 속한 그룹을 카테고리(ALL, LEADER, MEMBER) 별로 조회한다.", responses = @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = BaseResponse.class))))
    @CommonApiResponse
    public ResponseEntity<BaseResponse<?>> getMyTeamByCategory(Authentication authentication, @RequestParam(required = false) String keyword, @RequestParam(required = false, defaultValue = "ALL") String category, @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        MyTeamResponse myTeamResponse = teamQueryService.getMyTeamByCategory(AuthenticationParser.parseUserId(authentication), keyword, category, pageable);

        return ResponseEntity.ok(new BaseResponse<>(myTeamResponse, LocalDateTime.now(ZoneId.of("Asia/Seoul")), "OK"));
    }

    @Operation(summary = "그룹(팀) 상세 조회", description = "내가 속한 팀의 정보를 상세 조회합니다.")
    @GetMapping("/{teamId}")
    public ResponseEntity<BaseResponse<?>> getTeamDetailById(Authentication authentication, @PathVariable Long teamId) {
        MyTeamDetailResponse myTeamDetailResponse = teamQueryService.getTeamDetailById(AuthenticationParser.parseUserId(authentication), teamId);

        return ResponseEntity.ok(new BaseResponse<>(myTeamDetailResponse, LocalDateTime.now(ZoneId.of("Asia/Seoul")), "OK"));
    }
}
