package com.jangburich.application.team.service;

import java.util.List;
import java.util.Optional;

import com.jangburich.infrastructure.repository.UserRepository;
import com.jangburich.presentation.team.dto.request.RegisterTeamRequest;
import com.jangburich.presentation.team.dto.response.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jangburich.domain.common.Status;
import com.jangburich.domain.entity.Team;
import com.jangburich.domain.entity.TeamLeader;
import com.jangburich.domain.entity.TeamType;
import com.jangburich.domain.entity.UserTeam;
import com.jangburich.infrastructure.repository.TeamRepository;
import com.jangburich.infrastructure.repository.UserTeamRepository;
import com.jangburich.domain.user.domain.User;
import com.jangburich.global.payload.Message;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TeamService {

    private static final String DEFAULT_PROFILE_IMAGE_URL = "https://github.com/user-attachments/assets/56565343-51f4-48b5-bf87-7585011d8de6";

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final UserTeamRepository userTeamRepository;

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
                                .accountNumber(registerTeamRequest.teamLeaderAccountNumber())
                                .bankName(registerTeamRequest.bankName())
                                .build()
                )
                .teamType(TeamType.valueOf(registerTeamRequest.teamType()))
                .build();

        Team saved = teamRepository.save(team);

        UserTeam userTeam = UserTeam.of(user, team);
        userTeamRepository.save(userTeam);

        return new TeamSecretCodeResponse(saved.getSecretCode());
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

    public TeamCodeResponse getTeamsWithSecretCode(String secretCode) {
        Team team = teamRepository.findBySecretCode(secretCode)
                .orElseThrow(() -> new RuntimeException("시크릿 코드가 존재하지 않습니다."));

        long count = userTeamRepository.findAllByTeam(team).size();

        List<String> profileImages = userTeamRepository.findAllByTeam(team)
                .stream()
                .map(userTeam -> userTeam.getUser().getProfileImageUrl())
                .limit(3)
                .toList();

        return new TeamCodeResponse(
                team.getName(),
                team.getCreatedAt(),
                team.getTeamType(),
                count,
                profileImages,
                team.getStatus()
        );
    }
}
