package com.jangburich.global.payload;

import com.jangburich.domain.entity.Event;
import lombok.Builder;
import lombok.Getter;

import java.time.format.DateTimeFormatter;

public class EventDto {

    @Getter
    @Builder
    public static class EventListResponse {
        private Long id;
        private String title;
        private String imageUrl;
        private String startDate;
        private String endDate;
        private Boolean isOngoing;
        private String createdAt;

        public static EventListResponse from(Event event) {
            return EventListResponse.builder()
                    .id(event.getId())
                    .title(event.getTitle())
                    .imageUrl(event.getImageUrl())
                    .startDate(event.getStartDate().format(DateTimeFormatter.ofPattern("yyyy.MM.dd")))
                    .endDate(event.getEndDate().format(DateTimeFormatter.ofPattern("yyyy.MM.dd")))
                    .isOngoing(event.isOngoing())
                    .createdAt(event.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy.MM.dd")))
                    .build();
        }
    }

    @Getter
    @Builder
    public static class EventDetailResponse {
        private Long id;
        private String title;
        private String content;
        private String imageUrl;
        private String startDate;
        private String endDate;
        private Boolean isOngoing;
        private String createdAt;
        private String updatedAt;

        public static EventDetailResponse from(Event event) {
            return EventDetailResponse.builder()
                    .id(event.getId())
                    .title(event.getTitle())
                    .content(event.getContent())
                    .imageUrl(event.getImageUrl())
                    .startDate(event.getStartDate().format(DateTimeFormatter.ofPattern("yyyy.MM.dd")))
                    .endDate(event.getEndDate().format(DateTimeFormatter.ofPattern("yyyy.MM.dd")))
                    .isOngoing(event.isOngoing())
                    .createdAt(event.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy.MM.dd")))
                    .updatedAt(event.getUpdatedAt().format(DateTimeFormatter.ofPattern("yyyy.MM.dd")))
                    .build();
        }
    }

    @Getter
    @Builder
    public static class EventPageResponse {
        private java.util.List<EventListResponse> events;
        private int currentPage;
        private int totalPages;
        private long totalElements;
        private boolean hasNext;
        private boolean hasPrevious;
    }
}
