package com.jangburich.presentation.team.controller;

import com.jangburich.application.team.service.TeamCommandService;
import com.jangburich.global.payload.CommonApiResponse;
import com.jangburich.utils.parser.AuthenticationParser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Team", description = "Team Command API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/teams")
public class TeamCommandController {

    private final TeamCommandService teamCommandService;

    @PostMapping("/{teamId}/delete")
    @Operation(summary = "팀 탈퇴", description = "내가 속한 그룹에서 탈퇴한다", responses = @ApiResponse(responseCode = "204", description = "No Content"))
    @CommonApiResponse
    public ResponseEntity<Void> deleteTeam(Authentication authentication, @PathVariable long teamId) {
        teamCommandService.deleteTeam(AuthenticationParser.parseUserId(authentication), teamId);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{teamId}/delete/{userId}")
    @Operation(summary = "멤버 내보내기", description = "멤버를 팀에서 탈퇴시킨다", responses = @ApiResponse(responseCode = "204", description = "No Content"))
    @CommonApiResponse
    public ResponseEntity<Void> deleteTeamMember(Authentication authentication, @PathVariable long teamId, @PathVariable long userId) {
        teamCommandService.deleteTeamMember(AuthenticationParser.parseUserId(authentication), teamId, userId);

        return ResponseEntity.noContent().build();
    }

}
