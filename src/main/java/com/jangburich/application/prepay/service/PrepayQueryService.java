package com.jangburich.application.prepay.service;

import com.jangburich.domain.entity.Store;
import com.jangburich.domain.entity.StoreTeam;
import com.jangburich.domain.repository.StoreRepository;
import com.jangburich.domain.repository.StoreTeamRepository;
import com.jangburich.domain.user.domain.User;
import com.jangburich.domain.user.repository.UserRepository;
import com.jangburich.global.error.DefaultException;
import com.jangburich.global.error.DefaultNullPointerException;
import com.jangburich.global.payload.ErrorCode;
import com.jangburich.presentation.prepay.dto.response.PrepaymentInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PrepayQueryService {
    private final StoreRepository storeRepository;
    private final StoreTeamRepository storeTeamRepository;
    private final UserRepository userRepository;

    @Transactional
    public PrepaymentInfoResponse getPrepayInfo(String userId, Long storeId, Long teamId) {
        User user = userRepository.findByProviderId(userId)
                .orElseThrow(() -> new DefaultNullPointerException(ErrorCode.INVALID_AUTHENTICATION));

        StoreTeam storeTeam = storeTeamRepository.findByStoreIdAndTeamId(storeId, teamId)
                .orElseThrow(() -> new DefaultException(ErrorCode.INVALID_OPTIONAL_ISPRESENT));

        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new DefaultException(ErrorCode.INVALID_STORE_ID));

        Integer remainPrepay = 0;
        if (storeTeam != null) {
            remainPrepay = storeTeam.getRemainPoint();
        }

        return PrepaymentInfoResponse.builder()
                .remainPrepay(remainPrepay)
                .minPrepayAmount(store.getMinPrepayment())
                .wallet(0) // TODO 수정 필요
                .category(store.getCategory().getDisplayName())
                .storeName(store.getName())
                .build();
    }
}
