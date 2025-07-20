package com.jangburich.application.store.service.command;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jangburich.application.store.resolver.StoreResolver;
import com.jangburich.domain.entity.Store;
import com.jangburich.domain.prepay.Prepay;
import com.jangburich.global.payload.ErrorCode;
import com.jangburich.infrastructure.exception.EntityNotFoundException;
import com.jangburich.infrastructure.repository.PrepayRepository;
import com.jangburich.presentation.store.dtos.response.store.StorePrepayTeamResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class StorePrepayTeamCommandService {
    private final StoreResolver storeResolver;

    private final PrepayRepository prepayRepository;

    public StorePrepayTeamResponse.approvalResponse approvePrepayTeam(String userId, Long teamId) {
        Store store = storeResolver.getStoreByUserId(userId);

        Prepay prepay = prepayRepository.findByStoreIdAndTeamId(store.getId(), teamId)
            .orElseThrow(() -> new EntityNotFoundException(ErrorCode.ENTITY_NOT_FOUND));

        prepay.prepayApproval();

        return StorePrepayTeamResponse.approvalResponse.builder()
            .prepayAmount(prepay.getPrepayAmount())
            .prepayStatus(prepay.getPrepayStatus())
            .approvalTime(prepay.getPrepayApprovalTime())
            .build();
    }
}
