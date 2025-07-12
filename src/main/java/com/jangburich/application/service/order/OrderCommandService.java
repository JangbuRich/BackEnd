package com.jangburich.application.service.order;

import com.amazonaws.services.kms.model.NotFoundException;
import com.jangburich.domain.entity.OrderStatus;
import com.jangburich.domain.entity.Orders;
import com.jangburich.domain.entity.Store;
import com.jangburich.domain.entity.StoreTeam;
import com.jangburich.domain.repository.OrdersRepository;
import com.jangburich.domain.repository.StoreRepository;
import com.jangburich.domain.repository.StoreTeamRepository;
import com.jangburich.domain.team.domain.Team;
import com.jangburich.domain.team.domain.repository.TeamRepository;
import com.jangburich.domain.user.domain.User;
import com.jangburich.domain.user.repository.UserRepository;
import com.jangburich.global.error.DefaultException;
import com.jangburich.global.payload.ErrorCode;
import com.jangburich.global.payload.Message;
import com.jangburich.presentation.order.dto.request.OrderRequest;
import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderCommandService {

    private final OrdersRepository ordersRepository;
    private final StoreRepository storeRepository;
    private final StoreTeamRepository storeTeamRepository;
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;

    @Transactional
    public long order(String userProviderId, OrderRequest orderRequest) {
        User user = userRepository.findByProviderId(userProviderId)
                .orElseThrow(()-> new DefaultException(ErrorCode.INVALID_USER_ID));

        Store store = storeRepository.findById(orderRequest.storeId())
                .orElseThrow(() -> new DefaultException(ErrorCode.INVALID_STORE_ID));

        Team team = teamRepository.findById(orderRequest.teamId())
                .orElseThrow(() -> new DefaultException(ErrorCode.INVALID_TEAM_ID));

        StoreTeam storeTeam = storeTeamRepository.findByStoreIdAndTeamId(store.getId(), team.getId())
                .orElseThrow(() -> new DefaultException(ErrorCode.INVALID_STORE_TEAM_ID));

        if(storeTeam.getPersonalAllocatedPoint() != null && storeTeam.getPersonalAllocatedPoint() < orderRequest.quantity()){
            throw new DefaultException(ErrorCode.INVALID_CHECK);
        }

        if(storeTeam.getRemainPoint() < orderRequest.quantity()){
            throw new DefaultException(ErrorCode.INVALID_CHECK);
        }

        storeTeam.setRemainPoint(storeTeam.getRemainPoint() - orderRequest.quantity());

        Orders orders = saveOrder(user, store, team, orderRequest);

        return orders.getId();
    }

    @Transactional
    public Message useMealTicket(String userProviderId, Long orderId) {
        User user = userRepository.findByProviderId(userProviderId)
                .orElseThrow(NullPointerException::new);

        Orders orders = ordersRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("식권을 찾을 수 없습니다"));

        orders.validateUser(user);

        orders.updateOrderStatus(OrderStatus.TICKET_USED);

        StoreTeam storeTeam = storeTeamRepository.findByStoreIdAndTeamId(orders.getStore().getId(),
                        orders.getTeam().getId())
                .orElseThrow(() -> new RuntimeException("store/team 연관이 없습니다."));

        int price = 0;

        storeTeam.usePoint(price);

        return Message.builder()
                .message("식권을 사용했습니다.")
                .build();
    }

    private Orders saveOrder(User user, Store store, Team team, OrderRequest orderRequest) {
        Orders orders = Orders.builder()
                .store(store)
                .user(user)
                .team(team)
                .orderStatus(OrderStatus.RECEIVED)
                .orderPrice(orderRequest.quantity())
                .build();
        try {
            return ordersRepository.save(orders);
        } catch (OptimisticLockException e) {
            throw new OptimisticLockException();
        }
    }
}
