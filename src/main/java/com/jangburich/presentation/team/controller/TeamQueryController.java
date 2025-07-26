package com.jangburich.presentation.team.controller;

import com.jangburich.application.team.service.TeamQueryService;
import com.jangburich.global.payload.ResponseCustom;
import com.jangburich.presentation.team.dto.response.TeamCodeResponse;
import com.jangburich.presentation.team.dto.response.TeamPaymentHistoryResponse;
import com.jangburich.presentation.team.dto.response.TeamPrepaidStoreItem;
import com.jangburich.presentation.team.dto.response.TeamPrepaidStoreResponse;
import com.jangburich.presentation.team.dto.response.myTeam.TeamMemberResponse;
import com.jangburich.presentation.team.dto.response.myTeam.MyTeamDetailResponse;
import com.jangburich.presentation.team.dto.response.myTeam.MyTeamResponse;
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

    @GetMapping("/{teamId}")
    @Operation(summary = "그룹(팀) 상세 조회", description = "내가 속한 팀의 정보를 상세 조회합니다.", responses = {@ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = BaseResponse.class)))})
    @CommonApiResponse
    public ResponseEntity<BaseResponse<?>> getTeamDetailById(Authentication authentication, @PathVariable Long teamId) {
        MyTeamDetailResponse myTeamDetailResponse = teamQueryService.getTeamDetailById(AuthenticationParser.parseUserId(authentication), teamId);

        return ResponseEntity.ok(new BaseResponse<>(myTeamDetailResponse, LocalDateTime.now(ZoneId.of("Asia/Seoul")), "OK"));
    }

    @GetMapping("/{teamId}/members")
    @Operation(summary = "그룹(팀) 멤버 전체 조회", description = "그룹(팀)에 소속된 모든 멤버를 조회합니다.", responses = {@ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = BaseResponse.class)))})
    @CommonApiResponse
    public ResponseEntity<BaseResponse<?>> getTeamMembers(Authentication authentication, @PathVariable Long teamId, @PageableDefault(page = 0, size = 10, sort = "user.name", direction = Sort.Direction.ASC) Pageable pageable) {
        TeamMemberResponse teamMemberResponse = teamQueryService.getTeamMembers(AuthenticationParser.parseUserId(authentication), teamId, pageable);
        return ResponseEntity.ok(new BaseResponse<>(teamMemberResponse, LocalDateTime.now(ZoneId.of("Asia/Seoul")), "OK"));
    }

    @GetMapping("/{teamId}/payment/history")
    @Operation(summary = "팀별 결제 내역 조회", description = "팀별 결제 내역을 조회합니다.", responses = {@ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = BaseResponse.class)))})
    @CommonApiResponse
    public ResponseEntity<BaseResponse<?>> getTeamPaymentHistory(Authentication authentication, @PathVariable Long teamId, @RequestParam(required = false) Long userId, @RequestParam(required = false) Long storeId, @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        TeamPaymentHistoryResponse teamPaymentHistoryResponse = teamQueryService.getTeamPaymentHistory(AuthenticationParser.parseUserId(authentication), teamId, userId, storeId, pageable);
        return ResponseEntity.ok(new BaseResponse<>(teamPaymentHistoryResponse, LocalDateTime.now(ZoneId.of("Asia/Seoul")), "OK"));
    }

    @GetMapping("/{teamId}/store")
    @Operation(summary = "제휴 매장 조회", description = "팀의 제휴 매장을 조회합니다.", responses = {@ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = BaseResponse.class)))})
    @CommonApiResponse
    public ResponseEntity<BaseResponse<?>> getTeamStoreList(Authentication authentication, @PathVariable Long teamId, @RequestParam(required = false) String keyword, @PageableDefault(page = 0, size = 3) Pageable pageable) {
        TeamPrepaidStoreResponse teamPrepaidStoreResponse = teamQueryService.getTeamStoreList(AuthenticationParser.parseUserId(authentication), teamId, keyword, pageable);

        return ResponseEntity.ok(new BaseResponse<>(teamPrepaidStoreResponse, LocalDateTime.now(ZoneId.of("Asia/Seoul")), "OK"));
    }

    @Operation(summary = "그룹(팀) 비밀코드 조회", description = "비밀코드를 입력하면, 그 팀을 조회하는 api 입니다.")
    @GetMapping("/info/secretcode/{secretCode}")
    public ResponseCustom<TeamCodeResponse> getTeamWithSecretCode(@PathVariable String secretCode) {
        return ResponseCustom.OK(teamQueryService.getTeamsWithSecretCode(secretCode));
    }
}
