package com.jangburich.domain.user.repository;

import static com.jangburich.domain.store.domain.QStoreTeam.storeTeam;
import static com.jangburich.domain.team.domain.QTeam.*;
import static com.jangburich.domain.team.domain.QUserTeam.userTeam;
import static com.jangburich.domain.user.domain.QUser.user;

import com.jangburich.domain.common.Status;
import com.jangburich.domain.user.dto.response.QTeamsResponse;
import com.jangburich.domain.user.dto.response.QUserHomeResponse;
import com.jangburich.domain.user.dto.response.TeamsResponse;
import com.jangburich.domain.user.dto.response.UserHomeResponse;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class UserQueryDslRepositoryImpl implements UserQueryDslRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public UserHomeResponse findUserHomeData(Long userId) {

        LocalDate currentDate = LocalDate.now();
        String formattedDate = currentDate.format(DateTimeFormatter.ISO_LOCAL_DATE);

        List<TeamsResponse> teamsResponses = queryFactory
                .selectDistinct(new QTeamsResponse(
                        userTeam.team.id,
                        storeTeam.store.id,
                        Expressions.numberTemplate(Integer.class,
                                "DATEDIFF({0}, CURRENT_DATE)",
                                storeTeam.prepaidExpirationDate),
                        storeTeam.store.representativeImage,
                        Expressions.constant(false),
                        storeTeam.team.name,
                        storeTeam.store.name,
                        storeTeam.point,
                        storeTeam.remainPoint,
                        storeTeam.createdAt
                ))
                .from(userTeam)
                .leftJoin(team).on(team.id.eq(userTeam.team.id))
                .leftJoin(storeTeam).on(storeTeam.team.eq(team))
                .where(storeTeam.status.eq(Status.ACTIVE),
                        userTeam.user.userId.eq(userId))
                .orderBy(storeTeam.createdAt.desc())
                .fetch();

        UserHomeResponse userHomeResponse = queryFactory.select(new QUserHomeResponse(
                        user.userId,
                        Expressions.constant(formattedDate),
                        user.name,
                        Expressions.constant(teamsResponses),
                        Expressions.constant(""),
                        userTeam.count().intValue()
                ))
                .from(user)
                .leftJoin(userTeam).on(userTeam.user.eq(user))
                .where(user.userId.eq(userId))
                .fetchOne();

        return userHomeResponse;
    }
}
