package com.jangburich.domain.entity;

import com.jangburich.domain.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "notification_settings")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class NotificationSetting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private Boolean pushNotificationEnabled = true; // 푸시 알림 전체

    @Column(nullable = false)
    private Boolean orderNotificationEnabled = true; // 주문 관련 알림

    @Column(nullable = false)
    private Boolean paymentNotificationEnabled = true; // 결제 관련 알림

    @Column(nullable = false)
    private Boolean eventNotificationEnabled = true; // 이벤트 알림

    @Column(nullable = false)
    private Boolean marketingNotificationEnabled = false; // 마케팅 알림

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Builder
    public NotificationSetting(User user, Boolean pushNotificationEnabled, Boolean orderNotificationEnabled,
                               Boolean paymentNotificationEnabled, Boolean eventNotificationEnabled,
                               Boolean marketingNotificationEnabled) {
        this.user = user;
        this.pushNotificationEnabled = pushNotificationEnabled != null ? pushNotificationEnabled : true;
        this.orderNotificationEnabled = orderNotificationEnabled != null ? orderNotificationEnabled : true;
        this.paymentNotificationEnabled = paymentNotificationEnabled != null ? paymentNotificationEnabled : true;
        this.eventNotificationEnabled = eventNotificationEnabled != null ? eventNotificationEnabled : true;
        this.marketingNotificationEnabled = marketingNotificationEnabled != null ? marketingNotificationEnabled : false;
    }

    public void updateSettings(Boolean pushNotificationEnabled, Boolean orderNotificationEnabled, 
                             Boolean paymentNotificationEnabled, Boolean eventNotificationEnabled, 
                             Boolean marketingNotificationEnabled) {
        if (pushNotificationEnabled != null) this.pushNotificationEnabled = pushNotificationEnabled;
        if (orderNotificationEnabled != null) this.orderNotificationEnabled = orderNotificationEnabled;
        if (paymentNotificationEnabled != null) this.paymentNotificationEnabled = paymentNotificationEnabled;
        if (eventNotificationEnabled != null) this.eventNotificationEnabled = eventNotificationEnabled;
        if (marketingNotificationEnabled != null) this.marketingNotificationEnabled = marketingNotificationEnabled;
    }
}
