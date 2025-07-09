package com.jangburich.application.store.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jangburich.domain.order.domain.OrderResponse;
import com.jangburich.domain.order.domain.OrderStatus;
import com.jangburich.domain.order.domain.Orders;
import com.jangburich.domain.order.domain.repository.CustomOrderRepository;
import com.jangburich.domain.order.domain.repository.OrdersRepository;
import com.jangburich.domain.owner.domain.entity.Owner;
import com.jangburich.domain.owner.domain.repository.OwnerRepository;
import com.jangburich.domain.point.domain.repository.PointTransactionRepository;
import com.jangburich.domain.entity.Store;
import com.jangburich.domain.entity.StoreTeam;
import com.jangburich.presentation.store.dtos.response.order.OrderDetailResponse;
import com.jangburich.presentation.store.dtos.response.order.OrderGetResponse;
import com.jangburich.presentation.store.dtos.response.payment.PaymentGroupDetailResponse;
import com.jangburich.presentation.store.dtos.response.store.StoreChargeHistoryResponse;
import com.jangburich.presentation.store.dtos.response.store.StoreGetResponse;
import com.jangburich.domain.store.exception.OrdersNotFoundException;
import com.jangburich.presentation.store.dtos.response.store.view.StoreHomeResponse;
import com.jangburich.domain.repository.StoreRepository;
import com.jangburich.domain.repository.StoreTeamRepository;
import com.jangburich.domain.team.domain.Team;
import com.jangburich.domain.team.domain.repository.TeamRepository;
import com.jangburich.domain.user.domain.User;
import com.jangburich.domain.user.repository.UserRepository;
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

    private final StoreRepository storeRepository;
    private final OwnerRepository ownerRepository;
    private final UserRepository userRepository;
    private final StoreTeamRepository storeTeamRepository;
    private final OrdersRepository ordersRepository;
    private final CustomOrderRepository customOrderRepository;
    private final PointTransactionRepository pointTransactionRepository;
    private final TeamRepository teamRepository;

    /**
     * Home 화면 결제된 팀을 조회한다.
     * @param userId Authentication ID
     * @return StoreHomeResponse.TodayPaymentTeam
     */
    public List<StoreHomeResponse.TodayPaymentTeam> getPaymentGroup(String userId) {
        User user = userRepository.findByProviderId(userId)
            .orElseThrow(() -> new DefaultNullPointerException(ErrorCode.INVALID_AUTHENTICATION));

        Owner owner = ownerRepository.findByUser(user)
            .orElseThrow(() -> new DefaultNullPointerException(ErrorCode.INVALID_AUTHENTICATION));

        Store store = storeRepository.findByOwner(owner)
            .orElseThrow(() -> new DefaultNullPointerException(ErrorCode.INVALID_AUTHENTICATION));

        List<StoreTeam> storeIdWithStoreAndTeam = storeTeamRepository.findByStoreIdWithStoreAndTeam(store.getId());

        return storeIdWithStoreAndTeam.stream()
            .map(storeTeam -> {
                    return StoreHomeResponse.TodayPaymentTeam.builder()
                        .teamName(storeTeam.getTeam().getName())
                        .teamDescription(storeTeam.getTeam().getDescription())
                        .remainingPrice(storeTeam.getRemainPoint())
                        .build();
            }
            ).toList();
    }

    /**
     * Home 화면 매장 고유 Code 를 반환한다.
     * @param authentication Authentication
     * @return StoreHomeResponse.UniqueCode
     */
    public StoreHomeResponse.UniqueCode getStoreUniqueCode (String authentication) {
        User user = userRepository.findByProviderId(authentication)
            .orElseThrow(() -> new DefaultNullPointerException(ErrorCode.INVALID_AUTHENTICATION));

        Owner owner = ownerRepository.findByUser(user)
            .orElseThrow(() -> new DefaultNullPointerException(ErrorCode.INVALID_AUTHENTICATION));

        Store store = storeRepository.findByOwner(owner)
            .orElseThrow(() -> new DefaultNullPointerException(ErrorCode.INVALID_AUTHENTICATION));

        String storeUniqueCode = store.getStoreUniqueCode();

        return StoreHomeResponse.UniqueCode.builder()
            .uniqueCode(storeUniqueCode)
            .build();
    }

    /**
     * Home 화면 오늘자 주문 내역을 보여준다
     * @param userId : Authentication ID
     * @return OrderTodayResponse - 오늘자 주문 내역 DTO
     */
    public StoreHomeResponse.TodayOrder getTodayOrders(String userId) {
        List<OrderGetResponse> orderGetRespons = new ArrayList<>();

        User user = userRepository.findByProviderId(userId)
            .orElseThrow(() -> new DefaultNullPointerException(ErrorCode.INVALID_AUTHENTICATION));

        Owner owner = ownerRepository.findByUser(user)
            .orElseThrow(() -> new DefaultNullPointerException(ErrorCode.INVALID_AUTHENTICATION));

        Store store = storeRepository.findByOwner(owner)
            .orElseThrow(() -> new DefaultNullPointerException(ErrorCode.INVALID_AUTHENTICATION));

        LocalDateTime startOfDay = LocalDate.now().atStartOfDay(); // 오늘 시작
        LocalDateTime endOfDay = LocalDate.now().plusDays(1).atStartOfDay(); // 내일 시작 (오늘의 끝)

        List<Orders> allByStore = ordersRepository.findOrdersByStoreAndTodayDateAndStatus(store.getId(), startOfDay,
            endOfDay, OrderStatus.PaymentStatus());

        for (Orders orders : allByStore) {
            OrderGetResponse newOrderGetResponse = OrderGetResponse.builder()
                .id(orders.getId())
                .name(orders.getUser().getName())
                .orderStatus(orders.getOrderStatus())
                .teamName(orders.getTeam().getName())
                .price(orders.getOrderPrice())
                .build();

            orderGetRespons.add(newOrderGetResponse);
        }

        return StoreHomeResponse.TodayOrder.of(orderGetRespons);
    }

    /**
     * 나의 장부를 보여준다.
     * @param authentication Authentication ID
     * @return StoreHomeResponse.AccountInfo - 장부 DTO
     */
    public StoreHomeResponse.AccountInfo getStoreAccountInfo (String authentication) {
        User user = userRepository.findByProviderId(authentication)
            .orElseThrow(() -> new DefaultNullPointerException(ErrorCode.INVALID_AUTHENTICATION));

        Owner owner = ownerRepository.findByUser(user)
            .orElseThrow(() -> new DefaultNullPointerException(ErrorCode.INVALID_AUTHENTICATION));

        Store store = storeRepository.findByOwner(owner)
            .orElseThrow(() -> new DefaultNullPointerException(ErrorCode.INVALID_AUTHENTICATION));

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
        User user = userRepository.findByProviderId(authentication)
            .orElseThrow(() -> new DefaultNullPointerException(ErrorCode.INVALID_AUTHENTICATION));

        Owner owner = ownerRepository.findByUser(user)
            .orElseThrow(() -> new DefaultNullPointerException(ErrorCode.INVALID_AUTHENTICATION));

        Store store = storeRepository.findByOwner(owner)
            .orElseThrow(() -> new DefaultNullPointerException(ErrorCode.INVALID_PARAMETER));

        if (!store.getOwner().getUser().getProviderId().equals(authentication)) {
            throw new DefaultNullPointerException(ErrorCode.INVALID_AUTHENTICATION);
        }

        return new StoreGetResponse().of(store);
    }

    public PaymentGroupDetailResponse getPaymentGroupDetail(String userId, Long teamId) {
        User user = userRepository.findByProviderId(userId)
            .orElseThrow(() -> new DefaultNullPointerException(ErrorCode.INVALID_AUTHENTICATION));

        Owner owner = ownerRepository.findByUser(user)
            .orElseThrow(() -> new DefaultNullPointerException(ErrorCode.INVALID_AUTHENTICATION));

        Store store = storeRepository.findByOwner(owner)
            .orElseThrow(() -> new DefaultNullPointerException(ErrorCode.INVALID_AUTHENTICATION));

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

    private int getTotalOrderPrice (List<Orders> ordersByStoreAndDate) {
        return ordersByStoreAndDate.stream()
            .mapToInt(Orders::getOrderPrice)
            .sum();
    }

    public OrderDetailResponse getOrderDetails(String userId, Long orderId) {

        userRepository.findByProviderId(userId)
            .orElseThrow(() -> new DefaultNullPointerException(ErrorCode.INVALID_AUTHENTICATION));

        Orders orders = ordersRepository.findById(orderId).orElseThrow(OrdersNotFoundException::new);


        return OrderDetailResponse.builder()
            .id(orders.getId())
            .teamName(orders.getTeam().getName())
            .teamUserName(orders.getUser().getName())
            .dateTime(orders.getUpdatedAt())
            .amount(0) // TODO 수정 필요
            .totalPrice(0) // TODO 수정 필요
            .discountPrice(0) // TODO 수정 필요
            .build();
    }

    public List<OrderGetResponse> getOrdersLast(String userId) {
        List<OrderGetResponse> orderGetRespons = new ArrayList<>();

        User user = userRepository.findByProviderId(userId)
            .orElseThrow(() -> new DefaultNullPointerException(ErrorCode.INVALID_AUTHENTICATION));

        Owner owner = ownerRepository.findByUser(user)
            .orElseThrow(() -> new DefaultNullPointerException(ErrorCode.INVALID_AUTHENTICATION));

        Store store = storeRepository.findByOwner(owner)
            .orElseThrow(() -> new DefaultNullPointerException(ErrorCode.INVALID_AUTHENTICATION));

        LocalDateTime todayStart = LocalDate.now().atStartOfDay();
        List<Orders> allByStore = ordersRepository.findOrdersByStoreAndDateAndStatusNative(store.getId(), todayStart,
            "TICKET_USED");

        for (Orders orders : allByStore) {
            orderGetRespons.add(OrderGetResponse.builder()
                .id(orders.getId())
                .price(0) // TODO 수정 필요
                .build());
        }

        return orderGetRespons;
    }

    public List<StoreChargeHistoryResponse> getPaymentHistory(String userId) {
        User user = userRepository.findByProviderId(userId)
            .orElseThrow(() -> new DefaultNullPointerException(ErrorCode.INVALID_AUTHENTICATION));

        Owner owner = ownerRepository.findByUser(user)
            .orElseThrow(() -> new DefaultNullPointerException(ErrorCode.INVALID_AUTHENTICATION));

        Store store = storeRepository.findByOwner(owner)
            .orElseThrow(() -> new DefaultNullPointerException(ErrorCode.INVALID_AUTHENTICATION));

        return pointTransactionRepository.findAllByStore(store).stream()
            .sorted(Comparator.comparing(StoreChargeHistoryResponse::createdAt).reversed()) // 최신순 정렬
            .toList();
    }
}
