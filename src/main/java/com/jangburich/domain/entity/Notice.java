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
@Table(name = "notices")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Notice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private Boolean isImportant = false; // 중요 공지사항 여부

    @Column(nullable = false)
    private Boolean isActive = true; // 활성화 여부

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Builder
    public Notice(String title, String content, Boolean isImportant, Boolean isActive) {
        this.title = title;
        this.content = content;
        this.isImportant = isImportant != null ? isImportant : false;
        this.isActive = isActive != null ? isActive : true;
    }

    public void updateNotice(String title, String content, Boolean isImportant, Boolean isActive) {
        if (title != null) this.title = title;
        if (content != null) this.content = content;
        if (isImportant != null) this.isImportant = isImportant;
        if (isActive != null) this.isActive = isActive;
    }
}
