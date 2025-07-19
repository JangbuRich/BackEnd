package com.jangburich.application.store.service.query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jangburich.application.store.resolver.StoreResolver;
import com.jangburich.domain.entity.OrderResponse;
import com.jangburich.domain.entity.Orders;
import com.jangburich.domain.repository.CustomOrderRepository;
import com.jangburich.domain.repository.OrdersRepository;
import com.jangburich.domain.repository.PointTransactionRepository;
import com.jangburich.domain.entity.Store;
import com.jangburich.domain.entity.StoreTeam;
import com.jangburich.presentation.store.dtos.response.payment.PaymentGroupDetailResponse;
import com.jangburich.presentation.store.dtos.response.store.StoreChargeHistoryResponse;
import com.jangburich.presentation.store.dtos.response.store.StoreGetResponse;
import com.jangburich.presentation.store.dtos.response.store.view.StoreHomeResponse;
import com.jangburich.domain.repository.StoreTeamRepository;
import com.jangburich.domain.entity.Team;
import com.jangburich.domain.repository.TeamRepository;
import com.jangburich.domain.user.domain.User;
import com.jangburich.domain.repository.UserRepository;
import com.jangburich.global.error.DefaultNullPointerException;
import com.jangburich.global.payload.ErrorCode;
import com.jangburich.utils.DateTimeFormatterUtil;

import lombok.RequiredArgsConstructor;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class StoreQueryService {
    // StoreQueryService 가 목적에 맞지 않는 너무 많은 의존성을 가지고 있음 -> 분리해야함.
    // Todo: Order 관련 로직들은 Order 패키지로 옮겨져야 하는게 맞음 -> 추후 이동하기.
    // Todo: Team(group) 관련 로직들은 Team 패키지로 옮겨져야 하는게 맞음

    private final UserRepository userRepository;
    private final StoreTeamRepository storeTeamRepository;
    private final OrdersRepository ordersRepository;
    private final CustomOrderRepository customOrderRepository;
    private final PointTransactionRepository pointTransactionRepository;
    private final TeamRepository teamRepository;

    private final StoreResolver storeResolver;

    /**
     * Home 화면 결제된 팀을 조회한다.
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
     * Home 화면 매장 고유 Code 를 반환한다.
     * @param authentication Authentication
     * @return StoreHomeResponse.UniqueCode
     */
    public StoreHomeResponse.UniqueCode getStoreUniqueCode (String authentication) {
        Store store = storeResolver.getStoreByUserId(authentication);

        String storeUniqueCode = store.getStoreUniqueCode();

        return StoreHomeResponse.UniqueCode.builder()
            .uniqueCode(storeUniqueCode)
            .build();
    }

    /**
     * 나의 장부를 보여준다.
     * @param authentication Authentication ID
     * @return StoreHomeResponse.AccountInfo - 장부 DTO
     */
    public StoreHomeResponse.AccountInfo getStoreAccountInfo (String authentication) {
        Store store = storeResolver.getStoreByUserId(authentication);

        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().plusDays(1).atStartOfDay();

        List<Orders> ordersByStoreAndDate = customOrderRepository.queryOrdersByStoreIdAndStartDateAndEndDate(store.getId(), startOfDay, endOfDay);
        int totalOrderPrice = getTotalOrderPrice(ordersByStoreAndDate);

        return StoreHomeResponse.AccountInfo.builder()
            .today(DateTimeFormatterUtil.formatToKoreanDateTime(LocalDateTime.now()))
            .todayTotalOrderCount(ordersByStoreAndDate.size())
            .todayTotalOrderPrice(totalOrderPrice)
            .totalPrepayPrice(0)
            .newPrepayPrice(0)
            .newPrepayGroup(0)
            .build();
    }

    public StoreGetResponse getStoreInfo(String authentication) {
        Store store = storeResolver.getStoreByUserId(authentication);

        if (!store.getOwner().getUser().getProviderId().equals(authentication)) {
            throw new DefaultNullPointerException(ErrorCode.INVALID_AUTHENTICATION);
        }

        return new StoreGetResponse().of(store);
    }

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

    public List<StoreChargeHistoryResponse> getPaymentHistory(String userId) {
        Store store = storeResolver.getStoreByUserId(userId);

        return pointTransactionRepository.findAllByStore(store).stream()
            .sorted(Comparator.comparing(StoreChargeHistoryResponse::createdAt).reversed()) // 최신순 정렬
            .toList();
    }

    private int getTotalOrderPrice (List<Orders> ordersByStoreAndDate) {
        return ordersByStoreAndDate.stream()
            .mapToInt(Orders::getOrderPrice)
            .sum();
    }

}
