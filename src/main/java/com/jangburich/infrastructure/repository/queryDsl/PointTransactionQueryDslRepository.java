package com.jangburich.infrastructure.repository.queryDsl;

import java.time.LocalDateTime;
import java.util.List;

import com.jangburich.presentation.prepay.dto.response.PrepayResponse;

public interface PointTransactionQueryDslRepository {
    List<PrepayResponse.StorePrepayInfo> queryAllByStoreIdOrderByIdDesc(Long storeId, String name);

    List<PrepayResponse.StorePrepayInfo> queryAllByStoreIdAndBetweenStartDateAndEndDateOrderByIdDesc(Long storeId, LocalDateTime startDate, LocalDateTime endDate);

    Integer queryTotalAmountByStoreIdAndTeam(Long storeId, Long teamId);
}
