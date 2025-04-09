package com.jangburich.domain.team.domain.repository;

import com.jangburich.domain.team.dto.response.IndividualStoreDetailsResponse;
import com.jangburich.domain.team.dto.response.MyTeamDetailResponse;

public interface TeamQueryDslRepository {
    MyTeamDetailResponse findMyTeamDetailsAsMember(Long userId, Long teamId);

    MyTeamDetailResponse findMyTeamDetailsAsLeader(Long userId, Long teamId);

    IndividualStoreDetailsResponse findIndividualStoreDetails(Long userId, Long teamId, Long storeId, boolean isMeLeader);
}
