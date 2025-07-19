package com.jangburich.domain.repository.queryDsl;

import com.jangburich.presentation.team.dto.response.IndividualStoreDetailsResponse;
import com.jangburich.presentation.team.dto.response.MyTeamDetailResponse;

public interface TeamQueryDslRepository {
    MyTeamDetailResponse findMyTeamDetailsAsMember(Long userId, Long teamId);

    MyTeamDetailResponse findMyTeamDetailsAsLeader(Long userId, Long teamId);

    IndividualStoreDetailsResponse findIndividualStoreDetails(Long userId, Long teamId, Long storeId, boolean isMeLeader);
}
