package com.jangburich.application.store.service.query;

import java.util.Comparator;
import java.util.List;

import com.jangburich.domain.entity.*;
import com.jangburich.domain.payment.domain.TeamChargeHistory;
import com.jangburich.domain.payment.domain.repository.TeamChargeHistoryRepository;
import com.jangburich.infrastructure.exception.EntityNotFoundException;
import com.jangburich.infrastructure.repository.PointTransactionRepository;
import com.jangburich.infrastructure.repository.UserRepository;
import com.jangburich.infrastructure.repository.queryDsl.PointTransactionQueryDslRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jangburich.application.store.resolver.StoreResolver;
import com.jangburich.infrastructure.repository.OrdersRepository;
import com.jangburich.infrastructure.repository.StoreTeamRepository;
import com.jangburich.domain.team.domain.Team;
import com.jangburich.domain.team.domain.repository.TeamRepository;
import com.jangburich.domain.user.domain.User;
import com.jangburich.global.error.DefaultNullPointerException;
import com.jangburich.global.payload.ErrorCode;
import com.jangburich.presentation.store.dtos.response.payment.PaymentGroupDetailResponse;
import com.jangburich.presentation.store.dtos.response.store.view.StoreHomeResponse;

import lombok.RequiredArgsConstructor;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class StoreTeamQueryService {
    private final StoreResolver storeResolver;

    private final StoreTeamRepository storeTeamRepository;
    private final TeamRepository teamRepository;
    private final OrdersRepository ordersRepository;
    private final UserRepository userRepository;
    private final PointTransactionQueryDslRepository pointTransactionQueryDslRepository;
    private final PointTransactionRepository pointTransactionRepository;
    private final TeamChargeHistoryRepository teamChargeHistoryRepository;

    /**
     * Home 화면 결제된 팀을 조회한다.
     *
     * @param userId Authentication ID
     * @return StoreHomeResponse.TodayPaymentTeam
     */
    public List<StoreHomeResponse.TodayPaymentTeam> getPaymentGroup(String userId) {
        Store store = storeResolver.getStoreByUserId(userId);

        List<StoreTeam> storeIdWithStoreAndTeam = storeTeamRepository.findByStoreIdWithStoreAndTeam(store.getId());

        return storeIdWithStoreAndTeam.stream()
                .map(storeTeam -> StoreHomeResponse.TodayPaymentTeam.builder()
                        .teamName(storeTeam.getTeam().getName())
                        .teamDescription(storeTeam.getTeam().getDescription())
                        .remainingPrice(storeTeam.getRemainPoint())
                        .build()
                ).toList();
    }

    /**
     * 결제 그룹 상세정보를 조회한다.
     *
     * @param userId userId 인증 정보
     * @param teamId team 고유 id
     * @return
     */
    public PaymentGroupDetailResponse getPaymentGroupDetail(String userId, Long teamId) {
        Store store = storeResolver.getStoreByUserId(userId);

        // Todo: QueryDSL 로 한번에 조인해서 가져오는게 나을듯?
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.INVALID_PARAMETER));

        User teamLeader = userRepository.findById(team.getTeamLeader().getLeaderId())
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.INVALID_PARAMETER));

        StoreTeam storeTeam = storeTeamRepository.findByStoreIdAndTeamId(store.getId(), team.getId())
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.INVALID_PARAMETER));

        PointTransaction pointTransaction = pointTransactionRepository.findByStoreIdAndTeamId(store.getId(), team.getId())
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.ENTITY_NOT_FOUND));

        TeamChargeHistory teamChargeHistory = teamChargeHistoryRepository.findByTransactionId(String.valueOf(pointTransaction.getId()))
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.ENTITY_NOT_FOUND));

        Integer todayTotalAmount = pointTransactionQueryDslRepository.queryTotalAmountByStoreIdAndTeam(store.getId(), team.getId());

        List<Orders> orders = ordersRepository.findAllByTeam(team);

        List<OrderResponse> historyChargeList = getHistoryChargeList(orders, team, storeTeam);
        List<OrderResponse> historyPaymentList = getHistoryPaymentList(orders, team, pointTransaction, teamChargeHistory);

        return PaymentGroupDetailResponse.create(
                team, todayTotalAmount, storeTeam.getRemainPoint(), teamLeader, historyChargeList, historyPaymentList
        );
    }

    private List<OrderResponse> getHistoryChargeList(List<Orders> orders, Team team, StoreTeam storeTeam) {
        return orders.stream()
                .map(order -> {
                    return new OrderResponse(
                            order.getId(), team.getName(), order.getUser().getName(),
                            order.getCreatedAt().toLocalDate(), storeTeam.getPoint(), ""
                    );
                })
                .sorted(Comparator.comparing(OrderResponse::date).reversed())
                .toList();
    }

    private List<OrderResponse> getHistoryPaymentList(List<Orders> orders, Team team, PointTransaction pointTransaction, TeamChargeHistory teamChargeHistory) {
        return orders.stream()
                .map(order -> {
                    return new OrderResponse(
                            order.getId(), team.getName(), order.getUser().getName(),
                            order.getCreatedAt().toLocalDate(), pointTransaction.getTransactionedPoint(),
                            teamChargeHistory.getPaymentChargeStatus().name()
                    );
                })
                .sorted(Comparator.comparing(OrderResponse::date).reversed())
                .toList();
    }

}
