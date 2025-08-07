package com.jangburich.global.payload;

import com.jangburich.domain.entity.Faq;
import lombok.Builder;
import lombok.Getter;

public class FaqDto {

    @Getter
    @Builder
    public static class FaqResponse {
        private Long id;
        private String question;
        private String answer;
        private String category;
        private String categoryDescription;
        private Integer displayOrder;

        public static FaqResponse from(Faq faq) {
            return FaqResponse.builder()
                    .id(faq.getId())
                    .question(faq.getQuestion())
                    .answer(faq.getAnswer())
                    .category(faq.getCategory().name())
                    .categoryDescription(faq.getCategory().getDescription())
                    .displayOrder(faq.getDisplayOrder())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class FaqCategoryResponse {
        private String category;
        private String categoryDescription;
        private java.util.List<FaqResponse> faqs;
    }

    @Getter
    @Builder
    public static class FaqListResponse {
        private java.util.List<FaqCategoryResponse> categories;
        private long totalCount;
    }
}
