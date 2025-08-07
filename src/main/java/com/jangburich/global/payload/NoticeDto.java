package com.jangburich.global.payload;

import com.jangburich.domain.entity.Notice;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class NoticeDto {

    @Getter
    @Builder
    public static class NoticeListResponse {
        private Long id;
        private String title;
        private Boolean isImportant;
        private String createdAt;

        public static NoticeListResponse from(Notice notice) {
            return NoticeListResponse.builder()
                    .id(notice.getId())
                    .title(notice.getTitle())
                    .isImportant(notice.getIsImportant())
                    .createdAt(notice.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy.MM.dd")))
                    .build();
        }
    }

    @Getter
    @Builder
    public static class NoticeDetailResponse {
        private Long id;
        private String title;
        private String content;
        private Boolean isImportant;
        private String createdAt;
        private String updatedAt;

        public static NoticeDetailResponse from(Notice notice) {
            return NoticeDetailResponse.builder()
                    .id(notice.getId())
                    .title(notice.getTitle())
                    .content(notice.getContent())
                    .isImportant(notice.getIsImportant())
                    .createdAt(notice.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy.MM.dd")))
                    .updatedAt(notice.getUpdatedAt().format(DateTimeFormatter.ofPattern("yyyy.MM.dd")))
                    .build();
        }
    }

    @Getter
    @Builder
    public static class NoticePageResponse {
        private java.util.List<NoticeListResponse> notices;
        private int currentPage;
        private int totalPages;
        private long totalElements;
        private boolean hasNext;
        private boolean hasPrevious;
    }
}
