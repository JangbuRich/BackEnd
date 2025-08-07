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
@Table(name = "terms")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Terms {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TermsType type;

    @Column(nullable = false, length = 10)
    private String version; // 약관 버전

    @Column(nullable = false)
    private Boolean isActive = true; // 활성화 여부

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Builder
    public Terms(String title, String content, TermsType type, String version, Boolean isActive) {
        this.title = title;
        this.content = content;
        this.type = type;
        this.version = version;
        this.isActive = isActive != null ? isActive : true;
    }

    public void updateTerms(String title, String content, String version, Boolean isActive) {
        if (title != null) this.title = title;
        if (content != null) this.content = content;
        if (version != null) this.version = version;
        if (isActive != null) this.isActive = isActive;
    }

    public enum TermsType {
        SERVICE("서비스 이용약관"),
        PRIVACY("개인정보처리방침"),
        MARKETING("마케팅 정보 수신 동의"),
        LOCATION("위치정보 이용약관");

        private final String description;

        TermsType(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }
}
