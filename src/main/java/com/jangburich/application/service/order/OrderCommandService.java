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
import com.jangburich.presentation.order.dto.request.UseTicketRequest;
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
    public void useTicket(String userProviderId, Long orderId, UseTicketRequest useTicketRequest) {
        User user = userRepository.findByProviderId(userProviderId)
                .orElseThrow(()->new DefaultException(ErrorCode.INVALID_USER_ID));

        Orders orders = ordersRepository.findById(orderId)
                .orElseThrow(() -> new DefaultException(ErrorCode.INVALID_ORDER_ID));

        Store store = storeRepository.findById(useTicketRequest.storeId())
                .orElseThrow(()-> new DefaultException(ErrorCode.INVALID_STORE_ID));

        Team team = teamRepository.findById(useTicketRequest.teamId())
                        .orElseThrow(()-> new DefaultException(ErrorCode.INVALID_TEAM_ID));

        if(store!=orders.getStore() || team!=orders.getTeam()){
            throw new DefaultException(ErrorCode.INVALID_CHECK);
        }

        if(!store.getStoreUniqueCode().equals(useTicketRequest.secretCode())){
            throw new DefaultException(ErrorCode.INVALID_CHECK);
        }

        StoreTeam storeTeam = storeTeamRepository.findByStoreIdAndTeamId(store.getId(),
                        team.getId())
                .orElseThrow(() -> new DefaultException(ErrorCode.INVALID_STORE_TEAM_ID));

        orders.validateUser(user);

        orders.updateOrderStatus(OrderStatus.TICKET_USED);
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
