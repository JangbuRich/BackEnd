package com.jangburich.infrastructure.repository.queryDsl;

import com.jangburich.presentation.team.dto.response.*;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.jangburich.domain.entity.QPointTransaction.pointTransaction;
import static com.jangburich.domain.entity.QStore.store;
import static com.jangburich.domain.entity.QStoreTeam.storeTeam;
import static com.jangburich.domain.entity.QTeam.team;

@Slf4j
@RequiredArgsConstructor
@Repository
public class TeamQueryDslRepositoryImpl implements TeamQueryDslRepository {

    private final JPAQueryFactory queryFactory;

    LocalDate currentDate = LocalDate.now();
    String formattedDate = currentDate.format(DateTimeFormatter.ISO_LOCAL_DATE);

    LocalDateTime startOfDay = currentDate.atStartOfDay();
    LocalDateTime endOfDay = currentDate.plusDays(1).atStartOfDay().minusNanos(1);

    @Override
    public IndividualStoreDetailsResponse findIndividualStoreDetails(Long userId, Long teamId, Long storeId, boolean isMeLeader) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oneMonthAgo = now.minusMonths(1);


        if (!isMeLeader) {

            List<MyPaymentHistory> myPaymentHistories = queryFactory
                .select(new QMyPaymentHistory(
                    Expressions.stringTemplate(
                        "DATE_FORMAT({0}, '%m.%d')", pointTransaction.createdAt
                    ),
                    Expressions.stringTemplate(
                        "DATE_FORMAT({0}, '%H:%i')", pointTransaction.createdAt
                    ),
                    Expressions.asNumber(1), // TODO 수정 필요
                    Expressions.nullExpression()
                ))
                .from(pointTransaction)
                .where(pointTransaction.store.id.eq(storeId),
                    pointTransaction.team.id.eq(teamId),
                    pointTransaction.user.userId.eq(userId),
                    pointTransaction.createdAt.between(oneMonthAgo, now))
                .fetch();

            log.info("myPaymentHistories: {}", myPaymentHistories);

            Integer totalPrice = queryFactory
                .select(pointTransaction.transactionedPoint.sum())
                .from(pointTransaction)
                .where(pointTransaction.store.id.eq(storeId),
                    pointTransaction.team.id.eq(teamId),
                    pointTransaction.user.userId.eq(userId),
                    pointTransaction.createdAt.between(oneMonthAgo, now))
                .fetchOne();

            log.info("totalPrice: {}", totalPrice);

            return queryFactory
                .selectDistinct(new QIndividualStoreDetailsResponse(
                    store.id,
                    Expressions.constant(false),
                    store.name,
                    Expressions.constant(false),
                    storeTeam.remainPoint,
                    storeTeam.personalAllocatedPoint,
                    Expressions.constant(totalPrice),
                    Expressions.nullExpression(),
                    Expressions.nullExpression(),
                    Expressions.nullExpression(),
                    Expressions.stringTemplate(
                        "DATE_FORMAT({0}, '%y.%m.%d')", oneMonthAgo
                    ),
                    Expressions.stringTemplate(
                        "DATE_FORMAT({0}, '%y.%m.%d')", now
                    ),
                    Expressions.constant(myPaymentHistories)
                ))
                .from(storeTeam)
                .leftJoin(store).on(storeTeam.store.id.eq(store.id))
                .leftJoin(team).on(team.id.eq(storeTeam.team.id))
                .leftJoin(pointTransaction).on(pointTransaction.store.id.eq(storeTeam.store.id),
                    pointTransaction.user.userId.eq(userId))
                .where(storeTeam.store.id.eq(storeId),
                    storeTeam.team.id.eq(teamId))
                .fetchOne();
        }

        // 리더일 때
        List<MyPaymentHistory> myPaymentHistories = queryFactory
            .select(new QMyPaymentHistory(
                Expressions.stringTemplate(
                    "DATE_FORMAT({0}, '%m.%d')", pointTransaction.createdAt
                ),
                Expressions.stringTemplate(
                    "DATE_FORMAT({0}, '%H:%i')", pointTransaction.createdAt
                ),
                Expressions.asNumber(1), // TODO 수정 필요
                store.name // TODO 수정 필요
            ))
            .from(pointTransaction)
            .where(pointTransaction.store.id.eq(storeId),
                pointTransaction.team.id.eq(teamId),
                pointTransaction.user.userId.eq(userId),
                pointTransaction.createdAt.between(oneMonthAgo, now))
            .fetch();

        log.info("myPaymentHistories: {}", myPaymentHistories);

        return queryFactory
            .selectDistinct(new QIndividualStoreDetailsResponse(
                store.id,
                Expressions.constant(true),
                store.name,
                Expressions.constant(false),
                Expressions.nullExpression(),
                Expressions.nullExpression(),
                Expressions.nullExpression(),
//                        pointTransaction.transactionedPoint.sum(),
                Expressions.constant(660000),
//                        team.point,
                Expressions.constant(615600),
                storeTeam.personalAllocatedPoint,
                Expressions.stringTemplate(
                    "DATE_FORMAT({0}, '%y.%m.%d')", oneMonthAgo
                ),
                Expressions.stringTemplate(
                    "DATE_FORMAT({0}, '%y.%m.%d')", now
                ),
                Expressions.constant(myPaymentHistories)
            ))
            .from(storeTeam)
            .leftJoin(store).on(storeTeam.store.id.eq(store.id))
            .leftJoin(team).on(team.id.eq(storeTeam.team.id))
            .leftJoin(pointTransaction).on(pointTransaction.store.id.eq(storeTeam.store.id),
                pointTransaction.user.userId.eq(userId))
            .where(storeTeam.store.id.eq(storeId),
                storeTeam.team.id.eq(teamId))
            .fetchOne();
    }
}
