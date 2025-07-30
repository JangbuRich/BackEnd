package com.jangburich.domain.user.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_consent")
@Getter
@NoArgsConstructor
public class UserConsent implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_consent_id")
    private Long id;

    @Comment("마케팅 동의 여부")
    @Column(name = "marketing_consent", nullable = false)
    private Boolean marketingConsent = false;

    @Comment("광고 동의 여부")
    @Column(name = "advertising_consent", nullable = false)
    private Boolean advertisingConsent = false;

    @Comment("이메일 동의 여부")
    @Column(name = "email_consent", nullable = false)
    private Boolean emailConsent = false;

    @Comment("SNS 동의 여부")
    @Column(name = "sns_consent", nullable = false)
    private Boolean snsConsent = false;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT), nullable = false, unique = true)
    private User user;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Builder
    public UserConsent(Boolean marketingConsent, Boolean advertisingConsent, Boolean emailConsent, Boolean snsConsent, User user, LocalDateTime createdAt) {
        this.marketingConsent = marketingConsent;
        this.advertisingConsent = advertisingConsent;
        this.emailConsent = emailConsent;
        this.snsConsent = snsConsent;
        this.user = user;
        this.createdAt = createdAt;
    }
}
