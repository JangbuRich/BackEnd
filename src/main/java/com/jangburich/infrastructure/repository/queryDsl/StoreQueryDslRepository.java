package com.jangburich.infrastructure.repository.queryDsl;

import com.jangburich.domain.entity.Category;
import com.jangburich.presentation.store.dtos.response.store.StoreListItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StoreQueryDslRepository {
    Page<StoreListItem> findStoresByCategory(Long userId, Integer searchRadius, Category category, Double lat, Double lon, Pageable pageable);

    Page<StoreListItem> findStores(Long userId, String keyword, Pageable pageable);
}
