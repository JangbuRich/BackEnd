package com.jangburich.global.payload;

import com.jangburich.domain.entity.Terms;
import lombok.Builder;
import lombok.Getter;

import java.time.format.DateTimeFormatter;

public class TermsDto {

    @Getter
    @Builder
    public static class TermsResponse {
        private Long id;
        private String title;
        private String content;
        private String type;
        private String typeDescription;
        private String version;
        private String createdAt;
        private String updatedAt;

        public static TermsResponse from(Terms terms) {
            return TermsResponse.builder()
                    .id(terms.getId())
                    .title(terms.getTitle())
                    .content(terms.getContent())
                    .type(terms.getType().name())
                    .typeDescription(terms.getType().getDescription())
                    .version(terms.getVersion())
                    .createdAt(terms.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy.MM.dd")))
                    .updatedAt(terms.getUpdatedAt().format(DateTimeFormatter.ofPattern("yyyy.MM.dd")))
                    .build();
        }
    }

    @Getter
    @Builder
    public static class TermsListResponse {
        private java.util.List<TermsResponse> terms;
        private long totalCount;
    }
}
