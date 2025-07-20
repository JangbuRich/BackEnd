package com.jangburich.domain.repository.queryDsl;

import com.jangburich.domain.user.dto.response.UserHomeResponse;

public interface UserQueryDslRepository {
    UserHomeResponse findUserHomeData(Long userId);
}
