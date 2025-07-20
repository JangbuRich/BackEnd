package com.jangburich.domain.prepay;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.Comment;

import com.jangburich.domain.common.BaseEntity;
import com.jangburich.domain.entity.Store;
import com.jangburich.domain.prepay.enums.PrepayStatus;
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

@Comment("선결제")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Prepay extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Comment("선결제 신청 금액")
    @Column(name = "prepay_price", precision = 8)
    private BigDecimal prepayAmount;

    @Comment("선결제 신청 상태")
    @Enumerated(EnumType.STRING)
    @Column(name = "prepay_status")
    private PrepayStatus prepayStatus;

    @Comment("선결제 승인 시간")
    private LocalDateTime prepayApprovalTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    @Builder
    public Prepay (BigDecimal prepayAmount, PrepayStatus prepayStatus, User user, Store store) {
        this.prepayAmount = prepayAmount;
        this.prepayStatus = prepayStatus;
        this.user = user;
        this.store = store;
    }

    public void prepayApproval() {
        this.prepayStatus = PrepayStatus.APPROVED;
        this.prepayApprovalTime = LocalDateTime.now();
    }

}
