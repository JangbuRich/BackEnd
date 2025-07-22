package com.jangburich.application.team.service;

import com.jangburich.domain.common.Status;
import com.jangburich.domain.entity.Team;
import com.jangburich.domain.entity.UserTeam;
import com.jangburich.domain.user.domain.User;
import com.jangburich.global.error.DefaultException;
import com.jangburich.global.payload.ErrorCode;
import com.jangburich.infrastructure.repository.TeamRepository;
import com.jangburich.infrastructure.repository.UserRepository;
import com.jangburich.infrastructure.repository.UserTeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TeamCommandService {

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final UserTeamRepository userTeamRepository;

    @Transactional
    public void leaveTeam(String userId, long teamId) {
        User user = userRepository.findByProviderId(userId).orElseThrow(() -> new DefaultException(ErrorCode.INVALID_USER_ID));

        Team team = teamRepository.findById(teamId).orElseThrow(() -> new DefaultException(ErrorCode.INVALID_TEAM_ID));

        UserTeam userTeam = userTeamRepository.findByUserAndTeam(user, team).orElseThrow(() -> new DefaultException(ErrorCode.INVALID_USER_TEAM_ID));

        userTeam.updateStatus(Status.INACTIVE);
    }
}
