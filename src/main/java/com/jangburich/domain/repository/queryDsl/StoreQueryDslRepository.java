package com.jangburich.domain.repository.queryDsl;

import com.jangburich.domain.entity.Category;
import com.jangburich.domain.store.presentation.dto.response.store.SearchStoresResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StoreQueryDslRepository {
    Page<SearchStoresResponse> findStoresByCategory(Long userId, Integer searchRadius, Category category, Double lat, Double lon, Pageable pageable);

    Page<SearchStoresResponse> findStores(Long userId, String keyword, Pageable pageable);
}
