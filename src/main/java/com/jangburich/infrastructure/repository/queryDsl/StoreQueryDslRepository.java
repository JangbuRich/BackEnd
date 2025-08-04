package com.jangburich.infrastructure.repository.queryDsl;

import com.jangburich.domain.entity.Category;
import com.jangburich.domain.entity.Store;
import com.jangburich.domain.user.domain.User;
import com.jangburich.presentation.store.dtos.response.store.StoreDetailsResponse;
import com.jangburich.presentation.store.dtos.response.store.StoreListItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface StoreQueryDslRepository {
    Page<StoreListItem> findStoresByCategory(Long userId, Integer searchRadius, Category category, Double lat, Double lon, Pageable pageable);

    Page<StoreListItem> findStores(Long userId, String keyword, Pageable pageable);
}
