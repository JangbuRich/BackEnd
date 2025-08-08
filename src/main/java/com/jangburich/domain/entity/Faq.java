package com.jangburich.domain.entity;

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
@Table(name = "faqs")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Faq {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String question;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String answer;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FaqCategory category;

    @Column(nullable = false)
    private Integer displayOrder = 0; // 표시 순서

    @Column(nullable = false)
    private Boolean isActive = true; // 활성화 여부

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Builder
    public Faq(String question, String answer, FaqCategory category, Integer displayOrder, Boolean isActive) {
        this.question = question;
        this.answer = answer;
        this.category = category;
        this.displayOrder = displayOrder != null ? displayOrder : 0;
        this.isActive = isActive != null ? isActive : true;
    }

    public void updateFaq(String question, String answer, FaqCategory category, Integer displayOrder, Boolean isActive) {
        if (question != null) this.question = question;
        if (answer != null) this.answer = answer;
        if (category != null) this.category = category;
        if (displayOrder != null) this.displayOrder = displayOrder;
        if (isActive != null) this.isActive = isActive;
    }

    public enum FaqCategory {
        GENERAL("일반"),
        PAYMENT("결제"),
        PREPAY("선결제"),
        STORE("매장"),
        ACCOUNT("계정"),
        ETC("기타");

        private final String description;

        FaqCategory(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }
}
