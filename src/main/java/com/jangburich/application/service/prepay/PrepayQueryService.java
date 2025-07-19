package com.jangburich.application.service.prepay;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.jangburich.application.store.resolver.StoreResolver;
import com.jangburich.domain.entity.Store;
import com.jangburich.domain.entity.StoreTeam;
import com.jangburich.domain.point.domain.PointTransaction;
import com.jangburich.domain.point.domain.repository.PointTransactionRepository;
import com.jangburich.infrastructure.repository.StoreRepository;
import com.jangburich.infrastructure.repository.StoreTeamRepository;
import com.jangburich.domain.user.domain.User;
import com.jangburich.domain.user.repository.UserRepository;
import com.jangburich.global.error.DefaultException;
import com.jangburich.global.error.DefaultNullPointerException;
import com.jangburich.global.payload.ErrorCode;
import com.jangburich.infrastructure.repository.queryDsl.PointTransactionQueryDslRepository;
import com.jangburich.presentation.prepay.dto.response.PrepayResponse;
import com.jangburich.presentation.prepay.dto.response.PrepaymentInfoResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PrepayQueryService {
    private final StoreRepository storeRepository;
    private final StoreTeamRepository storeTeamRepository;
    private final UserRepository userRepository;
    private final PointTransactionRepository pointTransactionRepository;
    private final PointTransactionQueryDslRepository pointTransactionQueryDslRepository;

    private final StoreResolver storeResolver;

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

    public List<PrepayResponse.StorePrepayInfo> getStorePrepayInfo(String userId) {
        Store store = storeResolver.getStoreByUserId(userId);

        List<PointTransaction> storePointTransactionList = pointTransactionRepository.findAllByStoreIdOrderByIdDesc(store.getId());

        return buildStorePrepayInfoResponse(storePointTransactionList);
    }

    public List<PrepayResponse.StorePrepayInfo> getSearchStorePrepayInfoByName(String userId, String name) {
        Store store = storeResolver.getStoreByUserId(userId);

        return pointTransactionQueryDslRepository.queryAllByStoreIdOrderByIdDesc(store.getId(), name);
    }

    public List<PrepayResponse.StorePrepayInfo> getPrepayInfoFilterByDate (String userId, String startDate, String endDate) {
        Store store = storeResolver.getStoreByUserId(userId);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
        LocalDateTime startDateTime = LocalDate.parse(startDate, formatter).atStartOfDay();
        LocalDateTime endDateTime = LocalDate.parse(endDate, formatter).atTime(23,59,59);

        if(startDateTime.isAfter(endDateTime))
            throw new IllegalArgumentException("시작날짜는 종료날짜보다 길 수 없습니다.");

        return pointTransactionQueryDslRepository.queryAllByStoreIdAndBetweenStartDateAndEndDateOrderByIdDesc(store.getId(), startDateTime, endDateTime);
    }

    private List<PrepayResponse.StorePrepayInfo> buildStorePrepayInfoResponse (List<PointTransaction> storeList) {
        return storeList.stream()
            .map(point -> PrepayResponse.StorePrepayInfo.builder()
                .pointTransactionId(point.getId())
                .teamName(point.getTeam().getName())
                .userName(point.getUser().getName())
                .transactionPoint(point.getTransactionedPoint())
                .transactionType(point.getTransactionType())
                .build())
            .toList();
    }

}
