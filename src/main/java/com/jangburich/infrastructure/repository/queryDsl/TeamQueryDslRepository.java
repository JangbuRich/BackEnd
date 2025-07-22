package com.jangburich.infrastructure.repository.queryDsl;

import com.jangburich.presentation.team.dto.response.IndividualStoreDetailsResponse;

public interface TeamQueryDslRepository {

    IndividualStoreDetailsResponse findIndividualStoreDetails(Long userId, Long teamId, Long storeId, boolean isMeLeader);
}
