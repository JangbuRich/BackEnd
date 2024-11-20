package com.jangburich.domain.team.application;

import com.jangburich.domain.common.Status;
import com.jangburich.domain.team.dto.response.MyTeamResponse;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jangburich.domain.team.domain.Team;
import com.jangburich.domain.team.domain.TeamLeader;
import com.jangburich.domain.team.domain.TeamType;
import com.jangburich.domain.team.domain.UserTeam;
import com.jangburich.domain.team.domain.repository.TeamRepository;
import com.jangburich.domain.team.domain.repository.UserTeamRepository;
import com.jangburich.domain.team.dto.request.RegisterTeamRequest;
import com.jangburich.domain.user.domain.User;
import com.jangburich.domain.user.repository.UserRepository;
import com.jangburich.global.payload.Message;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TeamService {

	private static final int ZERO = 0;

	private final TeamRepository teamRepository;
	private final UserRepository userRepository;
	private final UserTeamRepository userTeamRepository;

	@Transactional
	public Message registerTeam(String userId, RegisterTeamRequest registerTeamRequest) {
		User user = userRepository.findByProviderId(userId)
			.orElseThrow(() -> new NullPointerException());

		Team team = Team.builder()
			.name(registerTeamRequest.teamName())
			.description(registerTeamRequest.description())
			.teamLeader(new TeamLeader(user.getUserId(), registerTeamRequest.teamLeaderAccountNumber(),
				registerTeamRequest.bankName()))
			.secretCode(registerTeamRequest.secretCode())
			.point(ZERO)
			.memberLimit(registerTeamRequest.memberLimit())
			.teamType(TeamType.valueOf(registerTeamRequest.teamType()))
			.build();

		teamRepository.save(team);

		UserTeam userTeam = UserTeam.of(user, team);
		userTeamRepository.save(userTeam);

		return Message.builder()
			.message("팀 생성이 완료되었습니다.")
			.build();
	}

	@Transactional
	public Message joinTeam(String userId, String joinCode) {
		User user = userRepository.findByProviderId(userId)
			.orElseThrow(() -> new NullPointerException());

		Team team = teamRepository.findBySecretCode(joinCode)
			.orElseThrow(() -> new IllegalArgumentException("Team not found"));

		team.validateJoinCode(joinCode);

		int currentMemberCount = userTeamRepository.countByTeam(team);
		team.validateMemberLimit(currentMemberCount);

		if (userTeamRepository.existsByUserAndTeam(user, team)) {
			throw new IllegalStateException("유저는 이미 해당 팀에 속해 있습니다.");
		}

		UserTeam userTeam = UserTeam.of(user, team);
		userTeamRepository.save(userTeam);

		return Message.builder()
			.message("팀에 성공적으로 참여하였습니다.")
			.build();
	}

	public List<MyTeamResponse> getMyTeamByCategory(String userId, String category) {
		User user = userRepository.findByProviderId(userId)
				.orElseThrow(() -> new NullPointerException());

		List<Team> teams = teamRepository.findAllByUserAndStatus(user, Status.ACTIVE)
				.orElseThrow(() -> new IllegalArgumentException("해당하는 팀을 찾을 수 없습니다."));

		List<MyTeamResponse> myTeamResponses = new ArrayList<>();

		for (Team team : teams) {
			boolean isMeLeader = team.getTeamLeader().getUser_id().equals(user.getUserId());

			int peopleCount = userTeamRepository.countByTeam(team);

			List<String> profileImageUrls = userTeamRepository.findAllByTeam(team).stream()
					.map(userTeam -> userTeam.getUser().getProfileImageUrl())
					.toList();

			if ("ALL".equalsIgnoreCase(category) || team.getTeamType().toString().equalsIgnoreCase(category)) {
				MyTeamResponse response = new MyTeamResponse(
						team.getName(),
						team.getTeamType().toString(),
						false, // isLiked는 임의로 false로 설정
						peopleCount,
						isMeLeader,
						profileImageUrls
				);
				myTeamResponses.add(response);
			}
		}

		return myTeamResponses;
	}
}
