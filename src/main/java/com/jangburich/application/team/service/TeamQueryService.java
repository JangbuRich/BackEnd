package com.jangburich.application.team.service;

import com.jangburich.domain.common.Status;
import com.jangburich.domain.entity.*;
import com.jangburich.global.error.DefaultException;
import com.jangburich.global.payload.ErrorCode;
import com.jangburich.global.payload.PageInfo;
import com.jangburich.infrastructure.repository.*;
import com.jangburich.presentation.team.dto.response.TeamPaymentHistoryItem;
import com.jangburich.presentation.team.dto.response.TeamPaymentHistoryResponse;
import com.jangburich.presentation.team.dto.response.myTeam.*;
import com.jangburich.domain.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeamQueryService {

    private static final String DEFAULT_PROFILE_IMAGE_URL = "https://github.com/user-attachments/assets/56565343-51f4-48b5-bf87-7585011d8de6";

    private final FavoriteTeamRepository favoriteTeamRepository;
    private final PointTransactionRepository pointTransactionRepository;
    private final StoreTeamRepository storeTeamRepository;
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final UserTeamRepository userTeamRepository;

    public MyTeamResponse getMyTeamByCategory(String userId, String keyword, String category, Pageable pageable) {
        User user = userRepository.findByProviderId(userId).orElseThrow(() -> new DefaultException(ErrorCode.INVALID_USER_ID));

        if (!StringUtils.hasText(keyword)) {
            keyword = null;
        }

        Page<Team> teams = teamRepository.findAllByUserAndCategory(user.getUserId(), keyword, category, pageable);

        List<Team> team = teams.getContent().stream().toList();

        Map<Long, Long> remainPointMap = storeTeamRepository.findRemainingPointByTeams(team).stream().collect(Collectors.toMap(row -> (Long) row[0], row -> (Long) row[1]));

        Set<Long> likedTeams = new HashSet<>(favoriteTeamRepository.findLikedTeamIdsByUser(user));

        Map<Long, Long> memberCountMap = userTeamRepository.countByTeams(team).stream().collect(Collectors.toMap(row -> (Long) row[0], row -> (Long) row[1]));

        Map<Long, List<String>> profileImageMap = userTeamRepository.findProfileImagesByTeams(team).stream().collect(Collectors.groupingBy(row -> (Long) row[0], Collectors.mapping(row -> Optional.ofNullable((String) row[1]).orElse(DEFAULT_PROFILE_IMAGE_URL), Collectors.collectingAndThen(Collectors.toList(), list -> list.stream().limit(3).toList()))));

        List<MyTeamItem> myTeamItemList = teams.stream().map(teamItem -> {
            Long teamId = teamItem.getId();

            return new MyTeamItem(teamId, teamItem.getName(), remainPointMap.getOrDefault(teamId, 0L), teamItem.getTeamType().name(), likedTeams.contains(teamId), memberCountMap.getOrDefault(teamId, 0L), user.getUserId().equals(teamItem.getTeamLeader().getLeaderId()), profileImageMap.getOrDefault(teamId, List.of()));
        }).toList();

        PageInfo pageInfo = new PageInfo(teams.getNumber(), teams.getSize(), teams.getTotalPages(), teams.getTotalElements(), teams.hasNext(), teams.hasPrevious());

        return new MyTeamResponse(myTeamItemList, pageInfo);
    }

    public MyTeamDetailResponse getTeamDetailById(String userId, long teamId) {
        User user = userRepository.findByProviderId(userId).orElseThrow(() -> new DefaultException(ErrorCode.INVALID_USER_ID));

        Team team = teamRepository.findById(teamId).orElseThrow(() -> new DefaultException(ErrorCode.INVALID_TEAM_ID));

        StoreTeam storeTeam = storeTeamRepository.findFavoriteStoreByUserAndTeam(user, team).orElseGet(() -> storeTeamRepository.findLastStoreTeamByUserAndTeam(user, team));

        List<String> profileImages = userTeamRepository.findProfileImagesByTeam(team);

        return MyTeamDetailResponse.builder().teamId(team.getId()).isMeLeader(user.getUserId().equals(team.getTeamLeader().getLeaderId())).teamName(team.getName()).description(team.getDescription()).totalPrepaidAmount(storeTeamRepository.sumPointByTeam(team)).remainingAmount(storeTeamRepository.sumRemainPointByTeam(team)).storeName(storeTeam.getStore().getName()).storePrepaidAmount(storeTeam.getPoint()).storeRemainingAmount(storeTeam.getRemainPoint()).teamMemberImgUrl(profileImages.subList(0, profileImages.size() <= 5 ? profileImages.size() : 5)).totalMemberCount(profileImages.size()).build();
    }

    public TeamMemberResponse getTeamMembers(String userId, Long teamId, Pageable pageable) {
        User user = userRepository.findByProviderId(userId).orElseThrow(() -> new DefaultException(ErrorCode.INVALID_USER_ID));

        Team team = teamRepository.findById(teamId).orElseThrow(() -> new DefaultException(ErrorCode.INVALID_TEAM_ID));

        Page<UserTeam> userTeamPage = userTeamRepository.findAllByTeamAndStatus(team, Status.ACTIVE, pageable);

        List<TeamMemberItem> userTeamList = userTeamPage.stream().map(userTeam -> {
            User member = userTeam.getUser();

            return new TeamMemberItem(member.getUserId(), member.getName(), member.getUserId().equals(user.getUserId()), team.getTeamLeader().getLeaderId().equals(member.getUserId()), Optional.ofNullable(member.getProfileImageUrl()).orElse(DEFAULT_PROFILE_IMAGE_URL));
        }).toList();

        PageInfo pageInfo = new PageInfo(userTeamPage.getNumber(), userTeamPage.getSize(), userTeamPage.getTotalPages(), userTeamPage.getTotalElements(), userTeamPage.hasNext(), userTeamPage.hasPrevious());

        return new TeamMemberResponse(userTeamList, pageInfo);
    }

    public TeamPaymentHistoryResponse getTeamPaymentHistory(String providerId, Long teamId, Long userId, Long storeId, Pageable pageable) {
        User user = userRepository.findByProviderId(providerId).orElseThrow(() -> new DefaultException(ErrorCode.INVALID_USER_ID));

        Team team = teamRepository.findById(teamId).orElseThrow(() -> new DefaultException(ErrorCode.INVALID_TEAM_ID));

        UserTeam userTeam = userTeamRepository.findByUserAndTeam(user, team).orElseThrow(() -> new DefaultException(ErrorCode.INVALID_USER_TEAM_ID));

        Page<TeamPaymentHistoryItem> teamPaymentHistoryItemPage = pointTransactionRepository.findAllByTeamAndUserAndStore(team, userId, storeId, pageable);

        LocalDateTime startDate = pointTransactionRepository.findMinCreatedAtByTeamAndUserAndStore(team, userId, storeId);

        PageInfo pageInfo = new PageInfo(teamPaymentHistoryItemPage.getNumber(), teamPaymentHistoryItemPage.getSize(), teamPaymentHistoryItemPage.getTotalPages(), teamPaymentHistoryItemPage.getTotalElements(), teamPaymentHistoryItemPage.hasNext(), teamPaymentHistoryItemPage.hasPrevious());

        return new TeamPaymentHistoryResponse(startDate, LocalDateTime.now(ZoneId.of("Asia/Seoul")), teamPaymentHistoryItemPage.stream().toList(), pageInfo);
    }
}
