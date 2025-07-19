package com.jangburich.presentation.team.controller;

import com.jangburich.presentation.team.dto.response.myTeam.MyTeamResponse;
import com.jangburich.global.payload.BaseResponse;
import com.jangburich.global.payload.CommonApiResponse;
import com.jangburich.global.payload.ResponseCustom;
import com.jangburich.utils.parser.AuthenticationParser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Team", description = "Team Query API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/teams")
public class TeamQueryController {

    private final TeamQueryService teamQueryService;

    @GetMapping
    @Operation(summary = "내가 속한 그룹 조회", description = "내가 속한 그룹을 카테고리(ALL, LEADER, MEMBER) 별로 조회한다."
    , responses = @ApiResponse(responseCode = "200", description = "OK",
            content = @Content(schema = @Schema(implementation = BaseResponse.class))))
    @CommonApiResponse
    public ResponseCustom<List<MyTeamResponse>> getMyTeamByCategory(
            Authentication authentication
            , @RequestParam(required = false) String keyword
            , @RequestParam(required = false, defaultValue = "ALL") String category
    ) {
        return ResponseCustom.OK(
                teamService.getMyTeamByCategory(AuthenticationParser.parseUserId(authentication), category));
    }
}
