package com.jangburich.infrastructure.repository.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.jangburich.domain.entity.QStore;
import com.jangburich.domain.point.domain.QPointTransaction;
import com.jangburich.domain.team.domain.QTeam;
import com.jangburich.domain.user.domain.QUser;
import com.jangburich.infrastructure.repository.queryDsl.PointTransactionQueryDslRepository;
import com.jangburich.presentation.prepay.dto.response.PrepayResponse;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class PointTransactionQueryDslRepositoryImpl implements PointTransactionQueryDslRepository {
    private static final QStore Q_STORE = QStore.store;
    private static final QPointTransaction Q_POINT_TRANSACTION = QPointTransaction.pointTransaction;
    private static final QTeam Q_TEAM = QTeam.team;
    private static final QUser Q_USER = QUser.user;

    private final JPAQueryFactory jpaQueryFactory;

    // Todo: 추후 위 쿼리들은 페이징 처리가 필요할 수도 있음.

    @Override
    public List<PrepayResponse.StorePrepayInfo> queryAllByStoreIdOrderByIdDesc (Long storeId, String name) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(Q_STORE.id.eq(storeId));

        if (StringUtils.hasText(name)) {
            builder.andAnyOf(
                Q_TEAM.name.containsIgnoreCase(name),
                Q_USER.name.containsIgnoreCase(name)
            );
        }

        return jpaQueryFactory
            .select(Projections.constructor(PrepayResponse.StorePrepayInfo.class,
                Q_POINT_TRANSACTION.id,
                Q_TEAM.name,
                Q_USER.name,
                Q_POINT_TRANSACTION.transactionedPoint,
                Q_POINT_TRANSACTION.transactionType
            ))
            .from(Q_POINT_TRANSACTION)
            .innerJoin(Q_POINT_TRANSACTION.store, Q_STORE)
            .innerJoin(Q_POINT_TRANSACTION.team, Q_TEAM)
            .innerJoin(Q_POINT_TRANSACTION.user, Q_USER)
            .where(builder)
            .orderBy(Q_POINT_TRANSACTION.id.desc())
            .fetch();
    }

    @Override
    public List<PrepayResponse.StorePrepayInfo> queryAllByStoreIdAndBetweenStartDateAndEndDateOrderByIdDesc (
        Long storeId, LocalDateTime startDate, LocalDateTime endDate) {

        return jpaQueryFactory
            .select(Projections.constructor(PrepayResponse.StorePrepayInfo.class,
                Q_POINT_TRANSACTION.id,
                Q_TEAM.name,
                Q_USER.name,
                Q_POINT_TRANSACTION.transactionedPoint,
                Q_POINT_TRANSACTION.transactionType
            ))
            .from(Q_POINT_TRANSACTION)
            .where(Q_POINT_TRANSACTION.store.id.eq(storeId)
                .and(Q_POINT_TRANSACTION.createdAt.between(startDate, endDate)))
            .orderBy(Q_POINT_TRANSACTION.id.desc())
            .fetch();
    }

}
