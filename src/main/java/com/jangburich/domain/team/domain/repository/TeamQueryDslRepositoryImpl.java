package com.jangburich.domain.team.domain.repository;

import static com.jangburich.domain.menu.domain.QMenu.menu;
import static com.jangburich.domain.order.domain.QCart.cart;
import static com.jangburich.domain.point.domain.QPointTransaction.pointTransaction;
import static com.jangburich.domain.store.domain.QStore.store;
import static com.jangburich.domain.store.domain.QStoreTeam.storeTeam;
import static com.jangburich.domain.team.domain.QTeam.team;
import static com.jangburich.domain.team.domain.QUserTeam.userTeam;
import static com.jangburich.domain.user.domain.QUser.user;

import com.jangburich.domain.common.Status;
import com.jangburich.domain.point.domain.TransactionType;
import com.jangburich.domain.store.domain.QStoreTeam;
import com.jangburich.domain.team.domain.Team;
import com.jangburich.domain.team.dto.response.IndividualStoreDetailsResponse;
import com.jangburich.domain.team.dto.response.MyPaymentHistory;
import com.jangburich.domain.team.dto.response.MyTeamDetailsResponse;
import com.jangburich.domain.team.dto.response.PrepayedStore;
import com.jangburich.domain.team.dto.response.QIndividualStoreDetailsResponse;
import com.jangburich.domain.team.dto.response.QMyPaymentHistory;
import com.jangburich.domain.team.dto.response.QMyTeamDetailsResponse;
import com.jangburich.domain.team.dto.response.QPrepayedStore;
import com.jangburich.domain.team.dto.response.QTodayPayment;
import com.jangburich.domain.team.dto.response.TodayPayment;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@RequiredArgsConstructor
@Repository
public class TeamQueryDslRepositoryImpl implements TeamQueryDslRepository {

    private final JPAQueryFactory queryFactory;


    LocalDate currentDate = LocalDate.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    String formattedDate = currentDate.format(formatter);

    LocalDateTime startOfDay = currentDate.atStartOfDay();
    LocalDateTime endOfDay = currentDate.plusDays(1).atStartOfDay().minusNanos(1);

    @Override
    public MyTeamDetailsResponse findMyTeamDetailsAsMember(Long userId, Long teamId) {

        List<PrepayedStore> prepayedStores = queryFactory
                .select(new QPrepayedStore(
                        store.id,
                        store.name,
                        store.representativeImage,
                        store.address,
                        Expressions.constant(false)
                ))
                .from(store)
                .leftJoin(storeTeam).on(storeTeam.team.id.eq(teamId))
                .fetch();

        List<String> images = queryFactory
                .select(user.profileImageUrl)
                .from(userTeam)
                .where(userTeam.team.id.eq(teamId))
                .fetch();

        List<TodayPayment> todayPayments = queryFactory
                .selectDistinct(new QTodayPayment(
                        Expressions.constant(formattedDate),
                        Expressions.stringTemplate("DATE_FORMAT({0}, '%H:%i')", pointTransaction.createdAt),
                        menu.name,
                        cart.user.name,
                        menu.price
                ))
                .from(pointTransaction)
                .leftJoin(store).on(store.id.eq(pointTransaction.store.id))
                .leftJoin(cart).on(cart.store.id.eq(store.id))
                .leftJoin(menu).on(menu.id.eq(cart.menu.id), cart.status.eq(Status.INACTIVE))
                .where(pointTransaction.createdAt.between(startOfDay, endOfDay), pointTransaction.transactionType.eq(
                        TransactionType.FOOD_PURCHASE))
                .fetch();

        return queryFactory
                .selectDistinct(new QMyTeamDetailsResponse(
                        storeTeam.team.id,
                        Expressions.constant(false),
                        storeTeam.store.name,
                        storeTeam.team.name,
                        storeTeam.team.description,
                        Expressions.constant(-1),
                        storeTeam.remainPoint,
                        storeTeam.personalAllocatedPoint,
                        pointTransaction.transactionedPoint.sum(),
                        Expressions.constant(prepayedStores),
                        Expressions.constant(images),
                        Expressions.constant(images.size()),
                        Expressions.constant(todayPayments),
                        Expressions.constant(todayPayments.size())
                ))
                .from(storeTeam)
                .leftJoin(team).on(storeTeam.team.id.eq(teamId))
                .leftJoin(userTeam).on(userTeam.team.id.eq(storeTeam.team.id))
                .leftJoin(pointTransaction).on(pointTransaction.transactionType.eq(TransactionType.FOOD_PURCHASE),
                        pointTransaction.user.userId.eq(userId))
                .where(storeTeam.team.id.eq(teamId))
                .fetchOne();

    }

    @Override
    public MyTeamDetailsResponse findMyTeamDetailsAsLeader(Long userId, Long teamId) {

        LocalDateTime startOfToday = LocalDate.now().atStartOfDay();
        LocalDateTime endOfToday = LocalDate.now().atTime(LocalTime.NOON);

        List<PrepayedStore> prepayedStores = queryFactory
                .select(new QPrepayedStore(
                        store.id,
                        store.name,
                        store.representativeImage,
                        store.address,
                        Expressions.constant(false)
                ))
                .from(store)
                .leftJoin(storeTeam).on(storeTeam.team.id.eq(teamId))
                .fetch();

        List<String> images = queryFactory
                .select(user.profileImageUrl)
                .from(userTeam)
                .where(userTeam.team.id.eq(teamId))
                .fetch();

        List<TodayPayment> todayPayments = queryFactory
                .selectDistinct(new QTodayPayment(
                        Expressions.constant(formattedDate),
                        Expressions.stringTemplate("DATE_FORMAT({0}, '%H:%i')", pointTransaction.createdAt),
                        menu.name,
                        cart.user.name,
                        menu.price
                ))
                .from(pointTransaction)
                .leftJoin(store).on(store.id.eq(pointTransaction.store.id))
                .leftJoin(cart).on(cart.store.id.eq(store.id))
                .leftJoin(menu).on(menu.id.eq(cart.menu.id), cart.status.eq(Status.INACTIVE))
                .where(pointTransaction.createdAt.between(startOfToday, endOfToday), pointTransaction.transactionType.eq(
                        TransactionType.FOOD_PURCHASE))
                .fetch();

        return queryFactory
                .selectDistinct(new QMyTeamDetailsResponse(
                        storeTeam.team.id,
                        Expressions.constant(true),
                        storeTeam.store.name,
                        storeTeam.team.name,
                        storeTeam.team.description,
                        storeTeam.point,
                        storeTeam.remainPoint,
                        Expressions.constant(-1),
                        pointTransaction.transactionedPoint.sum(),
                        Expressions.constant(prepayedStores),
                        Expressions.constant(images),
                        Expressions.constant(images.size()),
                        Expressions.constant(todayPayments),
                        Expressions.constant(todayPayments.size())
                ))
                .from(storeTeam)
                .leftJoin(team).on(storeTeam.team.id.eq(teamId))
                .leftJoin(userTeam).on(userTeam.team.id.eq(storeTeam.team.id))
                .leftJoin(pointTransaction).on(pointTransaction.transactionType.eq(TransactionType.FOOD_PURCHASE),
                        pointTransaction.user.userId.eq(userId))
                .where(storeTeam.team.id.eq(teamId))
                .fetchOne();

    }

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
                            menu.name,
                            menu.price,
                            Expressions.nullExpression()
                    ))
                    .from(pointTransaction)
                    .leftJoin(menu).on(pointTransaction.menuId.eq(menu.id))
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

            System.out.println("totalPrice = " + totalPrice);

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
                        menu.name,
                        menu.price,
                        pointTransaction.user.name
                ))
                .from(pointTransaction)
                .leftJoin(menu).on(pointTransaction.menuId.eq(menu.id))
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
