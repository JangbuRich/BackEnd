package com.jangburich.application.team.service;

import com.jangburich.domain.common.Status;
import com.jangburich.domain.entity.Team;
import com.jangburich.domain.entity.TeamLeader;
import com.jangburich.domain.entity.TeamType;
import com.jangburich.domain.entity.UserTeam;
import com.jangburich.domain.user.domain.User;
import com.jangburich.global.error.DefaultException;
import com.jangburich.global.payload.ErrorCode;
import com.jangburich.global.payload.Message;
import com.jangburich.infrastructure.repository.TeamRepository;
import com.jangburich.infrastructure.repository.UserRepository;
import com.jangburich.infrastructure.repository.UserTeamRepository;
import com.jangburich.presentation.team.dto.request.RegisterTeamRequest;
import com.jangburich.presentation.team.dto.response.TeamSecretCodeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class TeamCommandService {

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final UserTeamRepository userTeamRepository;

    @Transactional
    public void deleteTeam(String userId, long teamId) {
        User user = userRepository.findByProviderId(userId).orElseThrow(() -> new DefaultException(ErrorCode.INVALID_USER_ID));

        Team team = teamRepository.findById(teamId).orElseThrow(() -> new DefaultException(ErrorCode.INVALID_TEAM_ID));

        UserTeam userTeam = userTeamRepository.findByUserAndTeam(user, team).orElseThrow(() -> new DefaultException(ErrorCode.INVALID_USER_TEAM_ID));

        userTeam.updateStatus(Status.INACTIVE);
    }

    @Transactional
    public void deleteTeamMember(String leaderId, long teamId, long userId) {
        User leader = userRepository.findByProviderId(leaderId).orElseThrow(() -> new DefaultException(ErrorCode.INVALID_USER_ID));

        Team team = teamRepository.findByIdAndTeamLeaderId(teamId, leader.getUserId()).orElseThrow(() -> new DefaultException(ErrorCode.INVALID_CHECK));

        User user = userRepository.findById(userId).orElseThrow(() -> new DefaultException(ErrorCode.INVALID_USER_ID));

        UserTeam userTeam = userTeamRepository.findByUserAndTeam(user, team).orElseThrow(() -> new DefaultException(ErrorCode.INVALID_CHECK));

        userTeam.updateStatus(Status.INACTIVE);
    }

    @Transactional
    public Message joinTeam(String userId, String joinCode) {
        User user = userRepository.findByProviderId(userId)
                .orElseThrow(NullPointerException::new);

        Team team = teamRepository.findBySecretCode(joinCode)
                .orElseThrow(() -> new IllegalArgumentException("Team not found"));

        team.validateJoinCode(joinCode);

        if (userTeamRepository.existsByUserAndTeam(user, team)) {
            throw new IllegalStateException("유저는 이미 해당 팀에 속해 있습니다.");
        }

        UserTeam userTeam = UserTeam.of(user, team);
        userTeamRepository.save(userTeam);

        return Message.builder()
                .message("팀에 성공적으로 참여하였습니다.")
                .build();
    }

    @Transactional
    public TeamSecretCodeResponse registerTeam(String userId, RegisterTeamRequest registerTeamRequest) {
        User user = userRepository.findByProviderId(userId)
                .orElseThrow(NullPointerException::new);

        Team team = Team.builder()
                .name(registerTeamRequest.teamName())
                .description(registerTeamRequest.description())
                .teamLeader(
                        TeamLeader.builder()
                                .leaderId(user.getUserId())
                                .build()
                )
                .teamType(TeamType.valueOf(registerTeamRequest.teamType()))
                .build();

        Team saved = teamRepository.save(team);

        UserTeam userTeam = UserTeam.of(user, team);
        userTeamRepository.save(userTeam);

        return new TeamSecretCodeResponse(saved.getId(), saved.getSecretCode());
    }
}
