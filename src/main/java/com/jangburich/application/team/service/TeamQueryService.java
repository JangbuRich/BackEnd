package com.jangburich.application.team.service;

import com.jangburich.domain.common.Status;
import com.jangburich.domain.entity.Team;
import com.jangburich.domain.repository.TeamRepository;
import com.jangburich.domain.user.repository.UserRepository;
import com.jangburich.presentation.team.dto.response.myTeam.MyTeamResponse;
import com.jangburich.domain.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeamQueryService {

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;

    public List<MyTeamResponse> getMyTeamByCategory(String userId, String category) {
        User user = userRepository.findByProviderId(userId)
                .orElseThrow(() -> new NullPointerException("사용자를 찾을 수 없습니다."));

        List<Team> teams = teamRepository.findAllByUserAndStatus(user, Status.ACTIVE)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 팀을 찾을 수 없습니다."));

        List<MyTeamResponse> myTeamResponses = new ArrayList<>();

        for (Team team : teams) {
            boolean isMeLeader = team.getTeamLeader().getLeaderId().equals(user.getUserId());

            // TODO 반복문 내에 repository 접근은 리팩터링 필수
            int memberCount = userTeamRepository.countByTeam(team);

            List<String> profileImageUrls = userTeamRepository.findAllByTeam(team).stream()
                    .map(userTeam -> Optional.ofNullable(userTeam.getUser().getProfileImageUrl())
                            .orElse(DEFAULT_PROFILE_IMAGE_URL))
                    .toList();

            if ("ALL".equalsIgnoreCase(category) ||
                ("LEADER".equalsIgnoreCase(category) && isMeLeader) ||
                ("MEMBER".equalsIgnoreCase(category) && !isMeLeader)) {

                MyTeamResponse response = new MyTeamResponse(
                        team.getId(),
                        team.getName(),
                        team.getTeamType().getDescription(),
                        false, // isLiked는 임의로 false로 설정
                        memberCount,
                        isMeLeader,
                        profileImageUrls,
                        0 // TODO 그룹의 남은 돈
                );
                myTeamResponses.add(response);
            }
        }

        return myTeamResponses;
    }
}
