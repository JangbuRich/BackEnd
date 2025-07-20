package com.jangburich.application.store.service.query;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

import com.jangburich.infrastructure.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jangburich.application.store.resolver.StoreResolver;
import com.jangburich.domain.entity.OrderResponse;
import com.jangburich.domain.entity.Orders;
import com.jangburich.domain.entity.Store;
import com.jangburich.domain.entity.StoreTeam;
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

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new DefaultNullPointerException(ErrorCode.INVALID_PARAMETER));

        User teamLeader = userRepository.findById(team.getTeamLeader().getLeaderId())
                .orElseThrow(() -> new DefaultNullPointerException(ErrorCode.INVALID_PARAMETER));

        StoreTeam storeTeam = storeTeamRepository.findByStoreIdAndTeamId(store.getId(), team.getId())
                .orElseThrow(() -> new DefaultNullPointerException(ErrorCode.INVALID_PARAMETER));

        List<Orders> orders = ordersRepository.findAllByTeam(team);

        List<OrderResponse> orderResponse = orders.stream()
                .map(order -> {
                    int price = 0;
                    LocalDate date = order.getUpdatedAt().toLocalDate();
                    return new OrderResponse(order.getId(), order.getUser().getName(), date, String.valueOf(price));
                })
                .sorted(Comparator.comparing(OrderResponse::getDate).reversed())
                .toList();

        return PaymentGroupDetailResponse.create(team, storeTeam.getPoint(), storeTeam.getRemainPoint(),
                teamLeader, orderResponse);
    }

}
