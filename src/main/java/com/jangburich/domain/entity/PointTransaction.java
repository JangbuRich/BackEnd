package com.jangburich.domain.entity;

import com.jangburich.domain.common.BaseEntity;
import com.jangburich.domain.entity.Store;
import com.jangburich.domain.entity.Team;
import com.jangburich.domain.user.domain.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PointTransaction extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type")
    private TransactionType transactionType;

    @Column(name = "menu_id")
    private Long menuId;

    @Column(name = "transactioned_point")
    private Integer transactionedPoint;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;


    @Builder
    public PointTransaction(TransactionType transactionType, Long menuId, Integer transactionedPoint, Team team,
                            User user,
                            Store store) {
        this.transactionType = transactionType;
        this.menuId = menuId;
        this.transactionedPoint = transactionedPoint;
        this.team = team;
        this.user = user;
        this.store = store;
    }
}
