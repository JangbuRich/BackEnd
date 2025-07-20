package com.jangburich.application.store.service.query;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jangburich.application.store.resolver.StoreResolver;
import com.jangburich.domain.entity.Store;
import com.jangburich.domain.prepay.Prepay;
import com.jangburich.domain.team.domain.Team;
import com.jangburich.domain.team.domain.repository.TeamRepository;
import com.jangburich.global.payload.ErrorCode;
import com.jangburich.infrastructure.exception.EntityNotFoundException;
import com.jangburich.infrastructure.repository.PrepayRepository;
import com.jangburich.presentation.store.dtos.response.store.StorePrepayTeamResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class StorePrepayTeamQueryService {
    private final StoreResolver storeResolver;

    private final PrepayRepository prepayRepository;
    private final TeamRepository teamRepository;

    /**
     * 선결제 신청한 그룹을 조회 (승인되지 않은 그룹)
     * @param userId
     * @return StorePrepayTeamResponse.teamInfo
     */
    public List<StorePrepayTeamResponse.teamInfo> getStorePrepayApplicationTeam(String userId) {
        Store store = storeResolver.getStoreByUserId(userId);
        List<Prepay> prepay = prepayRepository.findAllByStoreIdWithStore(store.getId());

        return prepay.stream()
            .map(pre -> StorePrepayTeamResponse.teamInfo.builder()
                .teamName(pre.getTeam().getName())
                .teamDescription(pre.getTeam().getDescription())
                .prepayAmount(pre.getPrepayAmount())
                .build())
            .toList();
    }

    /**
     * 선결제 승인을 위한 그룹 조회
     * @param userId
     * @param teamId
     * @return StorePrepayTeamResponse.ApprovalDetail
     */
    public StorePrepayTeamResponse.ApprovalDetail getStorePrepayApplicationTeamDetail(String userId, Long teamId) {
        Store store = storeResolver.getStoreByUserId(userId);
        Team team = teamRepository.findById(teamId)
            .orElseThrow(() -> new EntityNotFoundException(ErrorCode.ENTITY_NOT_FOUND));

        Prepay prepay = prepayRepository.findByStoreIdAndTeamId(store.getId(), teamId)
            .orElseThrow(() -> new EntityNotFoundException(ErrorCode.ENTITY_NOT_FOUND));

        return StorePrepayTeamResponse.ApprovalDetail.builder()
            .teamName(team.getName())
            .userName(store.getOwner().getName())
            .registerTime(prepay.getCreatedAt())
            .prepayAmount(prepay.getPrepayAmount())
            .build();
    }

}
