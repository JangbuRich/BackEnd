package com.jangburich.domain.order.domain;

import com.jangburich.domain.common.BaseEntity;
import com.jangburich.domain.menu.domain.Menu;
import com.jangburich.domain.order.dto.request.AddCartRequest;
import com.jangburich.domain.store.domain.Store;
import com.jangburich.domain.user.domain.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Version;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Cart extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "quantity")
    private Integer quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orders_id")
    private Orders orders;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @Version
    private Long version;


    @Builder
    public Cart(Integer quantity, Orders orders, Menu menu, User user, Store store) {
        this.quantity = quantity;
        this.orders = orders;
        this.menu = menu;
        this.user = user;
        this.store = store;
    }

    public static void validateHasAnotherStoreItem(User user, AddCartRequest addCartRequest,
                                                   List<Cart> allByUserAndStatus) {
        for (Cart cart : allByUserAndStatus) {
            if (!cart.getStore().getId().equals(addCartRequest.storeId())) {
                throw new IllegalArgumentException("서로 다른 가게의 물건을 장바구니에 함께 담을 수 없습니다.");
            }
        }
    }

    public void updateQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void updateOrders(Orders orders) {
        this.orders = orders;
    }
}
