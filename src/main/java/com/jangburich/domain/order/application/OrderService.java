package com.jangburich.domain.order.application;

import com.amazonaws.services.kms.model.NotFoundException;
import com.jangburich.domain.order.domain.OrderStatus;
import com.jangburich.domain.order.domain.Orders;
import com.jangburich.domain.order.domain.repository.OrdersRepository;
import com.jangburich.domain.order.dto.request.OrderRequest;
import com.jangburich.domain.order.dto.response.OrderResponse;
import com.jangburich.domain.point.domain.repository.PointTransactionRepository;
import com.jangburich.domain.store.domain.Store;
import com.jangburich.domain.store.domain.StoreTeam;
import com.jangburich.domain.store.repository.StoreRepository;
import com.jangburich.domain.store.repository.StoreTeamRepository;
import com.jangburich.domain.team.domain.Team;
import com.jangburich.domain.team.domain.repository.TeamRepository;
import com.jangburich.domain.user.domain.User;
import com.jangburich.domain.user.repository.UserRepository;
import com.jangburich.global.payload.Message;
import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

	private final UserRepository userRepository;
	private final StoreRepository storeRepository;
	private final OrdersRepository ordersRepository;
	private final TeamRepository teamRepository;
	private final StoreTeamRepository storeTeamRepository;


	@Transactional
	public OrderResponse order(String userProviderId, OrderRequest orderRequest) {
		User user = userRepository.findByProviderId(userProviderId)
			.orElseThrow(NullPointerException::new);

		Store store = storeRepository.findById(orderRequest.storeId())
			.orElseThrow(() -> new IllegalArgumentException("유효하지 않은 가게 id 입니다."));

		Team team = teamRepository.findById(orderRequest.teamId())
			.orElseThrow(() -> new IllegalArgumentException("유효하지 않은 그룹 id 입니다."));

		StoreTeam storeTeam = storeTeamRepository.findByStoreIdAndTeamId(store.getId(), team.getId())
				.orElseThrow(() -> new IllegalArgumentException("유효하지 않은 가게 id와 팀 id 입니다."));

		Orders orders = saveOrder(user, store, team, orderRequest);

		return null; // TODO OrderResponse 구현
	}

	private Orders saveOrder(User user, Store store, Team team, OrderRequest orderRequest) {
		Orders orders = Orders.builder()
			.store(store)
			.user(user)
			.team(team)
			.orderStatus(OrderStatus.RECEIVED)
			.build();
		try {
			return ordersRepository.save(orders);
		} catch (OptimisticLockException e) {
		throw new IllegalStateException("중복 요청입니다. 이전 요청이 처리 중입니다.");
	}
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
}
