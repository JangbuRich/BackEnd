package com.jangburich.presentation.team.controller;

import com.jangburich.application.team.service.TeamCommandService;
import com.jangburich.global.payload.BaseResponse;
import com.jangburich.global.payload.CommonApiResponse;
import com.jangburich.presentation.team.dto.response.myTeam.MyTeamResponse;
import com.jangburich.utils.parser.AuthenticationParser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Tag(name = "Team", description = "Team Command API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/teams")
public class TeamCommandController {

    private final TeamCommandService teamCommandService;

    @PostMapping("/{teamId}/delete")
    @Operation(summary = "팀 탈퇴", description = "내가 속한 그룹에서 탈퇴한다"
            , responses = @ApiResponse(responseCode = "204", description = "No Content"))
    @CommonApiResponse
    public ResponseEntity<Void> leaveTeam(
            Authentication authentication
            , @PathVariable long teamId
    ) {
        teamCommandService.leaveTeam(AuthenticationParser.parseUserId(authentication), teamId);

        return ResponseEntity.noContent().build();
    }
}
