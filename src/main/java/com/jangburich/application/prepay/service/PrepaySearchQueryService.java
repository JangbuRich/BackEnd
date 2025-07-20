package com.jangburich.application.prepay.service;

import com.jangburich.application.store.resolver.StoreResolver;
import com.jangburich.domain.entity.Store;
import com.jangburich.infrastructure.repository.queryDsl.PointTransactionQueryDslRepository;
import com.jangburich.presentation.prepay.dto.response.PrepayResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PrepaySearchQueryService {
    private final PointTransactionQueryDslRepository pointTransactionQueryDslRepository;

    private final StoreResolver storeResolver;

    public List<PrepayResponse.StorePrepayInfo> getSearchStorePrepayInfoByName(String userId, String name) {
        Store store = storeResolver.getStoreByUserId(userId);

        return pointTransactionQueryDslRepository.queryAllByStoreIdOrderByIdDesc(store.getId(), name);
    }

    public List<PrepayResponse.StorePrepayInfo> getPrepayInfoFilterByDate(String userId, String startDate, String endDate) {
        Store store = storeResolver.getStoreByUserId(userId);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
        LocalDateTime startDateTime = LocalDate.parse(startDate, formatter).atStartOfDay();
        LocalDateTime endDateTime = LocalDate.parse(endDate, formatter).atTime(23, 59, 59);

        if (startDateTime.isAfter(endDateTime))
            throw new IllegalArgumentException("시작날짜는 종료날짜보다 길 수 없습니다.");

        return pointTransactionQueryDslRepository.queryAllByStoreIdAndBetweenStartDateAndEndDateOrderByIdDesc(store.getId(), startDateTime, endDateTime);
    }
}
