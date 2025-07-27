package com.jangburich.presentation.team.controller;

import com.jangburich.application.team.service.TeamCommandService;
import com.jangburich.global.payload.BaseResponse;
import com.jangburich.global.payload.CommonApiResponse;
import com.jangburich.global.payload.Message;
import com.jangburich.global.payload.ResponseCustom;
import com.jangburich.presentation.team.dto.request.RegisterTeamRequest;
import com.jangburich.presentation.team.dto.response.TeamCreateResponse;
import com.jangburich.presentation.team.dto.response.TeamSecretCodeResponse;
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
import java.time.LocalDateTime;
import java.time.ZoneId;

@Tag(name = "Team", description = "Team Command API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/teams")
public class TeamCommandController {

    private final TeamCommandService teamCommandService;

    @PostMapping
    @Operation(summary = "팀 생성", description = "팀을 생성한다. 팀 리더는 생성자", responses = @ApiResponse(responseCode = "201", description = "Created", content = @Content(schema = @Schema(implementation = BaseResponse.class))))
    public ResponseEntity<BaseResponse<?>> registerTeam(Authentication authentication, @RequestBody RegisterTeamRequest registerTeamRequest) {
        TeamCreateResponse teamCreateResponseResponse = teamCommandService.registerTeam(AuthenticationParser.parseUserId(authentication), registerTeamRequest);
        URI location = URI.create("/teams/" + teamCreateResponseResponse.id());
        return ResponseEntity.created(location).body(new BaseResponse<>(teamCommandService, LocalDateTime.now(ZoneId.of("Asia/Seoul")), "OK"));
    }

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

    @Operation(summary = "팀 가입", description = "비밀 코드를 입력해 팀에 가입한다.")
    @PostMapping("/join/{joinCode}")
    public ResponseCustom<Message> joinTeam(Authentication authentication, @PathVariable("joinCode") String joinCode) {
        return ResponseCustom.OK(teamCommandService.joinTeam(AuthenticationParser.parseUserId(authentication), joinCode));
    }
}
