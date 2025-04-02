package com.jangburich.domain.store.repository;

import com.jangburich.domain.store.domain.Category;
import com.jangburich.domain.store.dto.response.QSearchStoresResponse;
import com.jangburich.domain.store.dto.response.SearchStoresResponse;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.jangburich.domain.store.domain.QStore.store;

@RequiredArgsConstructor
@Repository
public class StoreQueryDslRepositoryImpl implements StoreQueryDslRepository {

    private static final int RADIUS_OF_EARTH_KM = 6371;

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<SearchStoresResponse> findStoresByCategory(Long userId, Integer searchRadius, Category category,
                                                           Double lat, Double lon, Pageable pageable) {
        double myCurrentLat = lat;
        double myCurrentLon = lon;

        BooleanExpression categoryCondition = isAllCategory(category);

        List<SearchStoresResponse> results = queryFactory
                .select(new QSearchStoresResponse(store.id, store.name, store.latitude, store.longitude, Expressions.FALSE, store.category,
                        Expressions.constant(1.0), Expressions.constant("open"),
                        store.closeTime.stringValue(), store.contactNumber, store.representativeImage))
                .from(store)
                .where(
                        categoryCondition,
                        withinSearchRadius(myCurrentLat, myCurrentLon, searchRadius, store.latitude, store.longitude)
                )
                .orderBy(store.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(store.count())
                .from(store)
                .where(
                        categoryCondition,
                        withinSearchRadius(myCurrentLat, myCurrentLon, searchRadius, store.latitude, store.longitude)
                );

        return PageableExecutionUtils.getPage(results, pageable, () -> countQuery.fetch().size());
    }

    @Override
    public Page<SearchStoresResponse> findStores(Long userId, String keyword,
                                                 Pageable pageable) {

        List<SearchStoresResponse> results = queryFactory
                .select(new QSearchStoresResponse(store.id, store.name, store.latitude, store.longitude, Expressions.FALSE, store.category,
                        Expressions.constant(1.0), Expressions.constant("open"),
                        store.closeTime.stringValue(), store.contactNumber, store.representativeImage))
                .from(store)
                .where(
                        store.name.contains(keyword)
                )
                .orderBy(store.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(store.count())
                .from(store)
                .where(
                        store.name.contains(keyword)
                );

        return PageableExecutionUtils.getPage(results, pageable, () -> countQuery.fetch().size());
    }


    private BooleanExpression isAllCategory(Category category) {
        return category == Category.ALL ? Expressions.TRUE : store.category.eq(category);
    }

    private BooleanExpression withinSearchRadius(double userLat, double userLng, int searchRadius,
                                                 com.querydsl.core.types.dsl.NumberPath<Double> storeLat,
                                                 com.querydsl.core.types.dsl.NumberPath<Double> storeLng) {
        return Expressions.numberTemplate(Double.class,
                        "({0} * acos(cos(radians({1})) * cos(radians({2})) * cos(radians({3}) - radians({4})) + sin(radians({1})) * sin(radians({2}))))",
                        RADIUS_OF_EARTH_KM, userLat, storeLat, storeLng, userLng)
                .loe(searchRadius);
    }
}