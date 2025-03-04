package com.jangburich.domain.order.application;

import com.jangburich.domain.point.domain.PointTransaction;
import com.jangburich.domain.point.domain.TransactionType;
import com.jangburich.domain.point.domain.repository.PointTransactionRepository;
import jakarta.persistence.OptimisticLockException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.amazonaws.services.kms.model.NotFoundException;
import com.jangburich.domain.common.Status;
import com.jangburich.domain.menu.domain.Menu;
import com.jangburich.domain.menu.repository.MenuRepository;
import com.jangburich.domain.order.domain.Cart;
import com.jangburich.domain.order.domain.OrderStatus;
import com.jangburich.domain.order.domain.Orders;
import com.jangburich.domain.order.domain.repository.CartRepository;
import com.jangburich.domain.order.domain.repository.OrdersRepository;
import com.jangburich.domain.order.dto.request.AddCartRequest;
import com.jangburich.domain.order.dto.request.OrderRequest;
import com.jangburich.domain.order.dto.response.CartResponse;
import com.jangburich.domain.order.dto.response.GetCartItemsResponse;
import com.jangburich.domain.order.dto.response.OrderResponse;
import com.jangburich.domain.store.domain.Store;
import com.jangburich.domain.store.domain.StoreTeam;
import com.jangburich.domain.store.repository.StoreRepository;
import com.jangburich.domain.store.repository.StoreTeamRepository;
import com.jangburich.domain.team.domain.Team;
import com.jangburich.domain.team.domain.repository.TeamRepository;
import com.jangburich.domain.user.domain.User;
import com.jangburich.domain.user.repository.UserRepository;
import com.jangburich.global.payload.Message;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

	private final CartRepository cartRepository;
	private final UserRepository userRepository;
	private final MenuRepository menuRepository;
	private final StoreRepository storeRepository;
	private final OrdersRepository ordersRepository;
	private final TeamRepository teamRepository;
	private final StoreTeamRepository storeTeamRepository;
	private final PointTransactionRepository pointTransactionRepository;

	@Transactional
	public Message addCart(String userProviderId, AddCartRequest addCartRequest) {
		User user = userRepository.findByProviderId(userProviderId)
			.orElseThrow(() -> new NullPointerException());

		Menu menu = menuRepository.findById(addCartRequest.menuId())
			.orElseThrow(() -> new IllegalArgumentException("등록된 메뉴를 찾을 수 없습니다. "));

		List<Cart> allByUserAndStatus = cartRepository.findAllByUserAndStatus(user, Status.ACTIVE);
		Cart.validateHasAnotherStoreItem(user, addCartRequest, allByUserAndStatus);

		Optional<Cart> optionalCart = cartRepository.findByUserIdAndMenuIdAndStatus(user.getUserId(), menu.getId(),
			Status.ACTIVE);

		Store store = storeRepository.findById(addCartRequest.storeId())
			.orElseThrow(() -> new IllegalArgumentException("유효하지 않은 가게 id 입니다."));

		if (optionalCart.isPresent()) {
			Cart existingCart = optionalCart.get();
			try {
				existingCart.updateQuantity(existingCart.getQuantity() + addCartRequest.quantity());
			} catch (OptimisticLockException e) {
				throw new IllegalStateException("중복 요청입니다. 이전 요청이 처리 중입니다.");
			}

			return Message.builder()
				.message("장바구니에 상품을 추가했습니다.")
				.build();
		}

		Cart newCart = Cart.builder()
			.quantity(addCartRequest.quantity())
			.menu(menu)
			.user(user)
			.store(store)
			.orders(null)
			.build();

		cartRepository.save(newCart);

		return Message.builder()
			.message("장바구니에 상품을 추가했습니다.")
			.build();
	}

	public CartResponse getCartItems(String userProviderId) {
		User user = userRepository.findByProviderId(userProviderId)
			.orElseThrow(() -> new NullPointerException());

		List<Cart> carts = cartRepository.findAllByUserAndStatus(user, Status.ACTIVE);

		if (carts.isEmpty()) {
			return CartResponse.of(null, null, null, List.of(), 0);
		}

		List<GetCartItemsResponse> cartItems = carts.stream()
			.map(cart -> GetCartItemsResponse.of(
					cart.getMenu().getId(),
					cart.getMenu().getImageUrl(),
					cart.getMenu().getName(),
					cart.getMenu().getDescription(),
					cart.getQuantity(),
					cart.getMenu().getPrice()
			))
			.toList();

		int discountAmount = 0;
		CartResponse cartResponse = CartResponse.of(carts.get(0).getStore().getId(), carts.get(0).getStore().getName(), carts.get(0).getStore().getCategory().getDisplayName(), cartItems, discountAmount);

		return cartResponse;
	}

	@Transactional
	public OrderResponse order(String userProviderId, OrderRequest orderRequest) {
		User user = userRepository.findByProviderId(userProviderId)
			.orElseThrow(() -> new NullPointerException());

		Store store = storeRepository.findById(orderRequest.storeId())
			.orElseThrow(() -> new IllegalArgumentException("유효하지 않은 가게 id 입니다."));

		Team team = teamRepository.findById(orderRequest.teamId())
			.orElseThrow(() -> new IllegalArgumentException("유효하지 않은 그룹 id 입니다."));

		List<Long> menuIds = orderRequest.items()
				.stream()
				.map(item -> item.menuId())
				.collect(Collectors.toList());

		List<Object[]> results = menuRepository.findPricesByMenuIds(menuIds);

		Map<Long, Integer> priceMap = results.stream()
				.collect(Collectors.toMap(
						result -> (Long) result[0], // menuId
						result -> (Integer) result[1] // price
				));

		int totalAmount = orderRequest.items()
				.stream()
				.mapToInt(item -> {
					Integer price = priceMap.get(item.menuId()); // menuId로 가격 조회
					if (price == null) {
						throw new IllegalArgumentException("Invalid menuId: " + item.menuId());
					}
					return price * item.quantity(); // 가격 * 수량
				})
				.sum();

		for (OrderRequest.OrderItemRequest item : orderRequest.items()) {
			PointTransaction pointTransaction = PointTransaction
					.builder()
					.transactionType(TransactionType.FOOD_PURCHASE)
					.transactionedPoint(totalAmount * -1)
					.team(team)
					.user(user)
					.store(store)
					.menuId(item.menuId())
					.build();

			pointTransactionRepository.save(pointTransaction);
		}

		StoreTeam storeTeam = storeTeamRepository.findByStoreIdAndTeamId(store.getId(), team.getId())
				.orElseThrow(() -> new IllegalArgumentException("유효하지 않은 가게 id와 팀 id 입니다."));

		storeTeam.useRemainPoint(totalAmount);

		Orders orders = saveOrder(user, store, team, orderRequest);

		List<Cart> allByUserAndStatus = cartRepository.findAllByUserAndStatus(user, Status.ACTIVE);
		System.out.println("allByUserAndStatus = " + allByUserAndStatus);

		syncCart(orderRequest, allByUserAndStatus, orders, user);
		OrderResponse ticket = ordersRepository.findTicket(orders.getId());

		List<Cart> cartsForStatusManage = cartRepository.findAllByUserAndStatus(user, Status.ACTIVE);

		cartsForStatusManage.stream()
				.forEach(cart -> cart.updateStatus(Status.INACTIVE));

		return ticket;
	}

	private void syncCart(OrderRequest orderRequest, List<Cart> carts, Orders orders, User user) {
		if (carts.isEmpty()) {
			for (OrderRequest.OrderItemRequest orderItemRequest : orderRequest.items()) {
				Menu menu = menuRepository.findById(orderItemRequest.menuId())
						.orElseThrow(() -> new IllegalArgumentException("해당 메뉴를 찾을 수 없습니다."));

				Cart newCart = Cart.builder()
						.quantity(orderItemRequest.quantity())
						.orders(orders)
						.menu(menu)
						.user(user)
						.store(orders.getStore())
						.build();

				cartRepository.save(newCart);
				cartRepository.flush();
			}
		} else {
			for (Cart cart : carts) {
			for (OrderRequest.OrderItemRequest orderItemRequest : orderRequest.items()) {
				if (orderItemRequest.menuId().equals(cart.getMenu().getId())) {
					cart.updateQuantity(orderItemRequest.quantity());
					cart.updateOrders(orders);
				} else {
					Menu menu = menuRepository.findById(orderItemRequest.menuId())
							.orElseThrow(() -> new IllegalArgumentException("해당 메뉴를 찾을 수 없습니다."));

					Cart newCart = Cart.builder()
							.quantity(orderItemRequest.quantity())
							.orders(orders)
							.menu(menu)
							.user(user)
							.store(cart.getStore())
							.build();

					cartRepository.save(newCart);
				}
			}

		}
		}
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
			.orElseThrow(() -> new NullPointerException());

		Orders orders = ordersRepository.findById(orderId)
			.orElseThrow(() -> new NotFoundException("식권을 찾을 수 없습니다"));

		orders.validateUser(user);

		orders.updateOrderStatus(OrderStatus.TICKET_USED);

		StoreTeam storeTeam = storeTeamRepository.findByStoreIdAndTeamId(orders.getStore().getId(),
				orders.getTeam().getId())
			.orElseThrow(() -> new RuntimeException("store/team 연관이 없습니다."));

		int price = 0;
		for (Cart cart : cartRepository.findAllByOrders(orders)) {
			price += cart.getMenu().getPrice();
		}

		storeTeam.setRemainPoint(storeTeam.getRemainPoint() - price);

		return Message.builder()
			.message("식권을 사용했습니다.")
			.build();
	}

	@Transactional
	public Message removeCart(String userProviderId) {
		User user = userRepository.findByProviderId(userProviderId)
				.orElseThrow(() -> new NullPointerException());

		List<Cart> allByUserAndStatus = cartRepository.findAllByUserAndStatus(user, Status.ACTIVE);

		cartRepository.deleteAll(allByUserAndStatus);

		return Message.builder()
				.message("장바구니를 비웠습니다.")
				.build();
	}
}
