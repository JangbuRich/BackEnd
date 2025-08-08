package com.jangburich.application.faq;

import com.jangburich.domain.entity.Faq;
import com.jangburich.global.payload.FaqDto;
import com.jangburich.infrastructure.repository.FaqRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FaqService {

    private final FaqRepository faqRepository;

    /**
     * FAQ 목록 조회 (카테고리별 그룹화)
     */
    public FaqDto.FaqListResponse getFaqs() {
        List<Faq> faqs = faqRepository.findByIsActiveTrueOrderByCategoryAscDisplayOrderAscCreatedAtDesc();

        // 카테고리별로 그룹화
        Map<Faq.FaqCategory, List<Faq>> faqsByCategory = faqs.stream()
                .collect(Collectors.groupingBy(Faq::getCategory));

        List<FaqDto.FaqCategoryResponse> categories = Arrays.stream(Faq.FaqCategory.values())
                .filter(faqsByCategory::containsKey)
                .map(category -> {
                    List<FaqDto.FaqResponse> categoryFaqs = faqsByCategory.get(category).stream()
                            .map(FaqDto.FaqResponse::from)
                            .collect(Collectors.toList());

                    return FaqDto.FaqCategoryResponse.builder()
                            .category(category.name())
                            .categoryDescription(category.getDescription())
                            .faqs(categoryFaqs)
                            .build();
                })
                .collect(Collectors.toList());

        return FaqDto.FaqListResponse.builder()
                .categories(categories)
                .totalCount(faqs.size())
                .build();
    }

    /**
     * 카테고리별 FAQ 조회
     */
    public List<FaqDto.FaqResponse> getFaqsByCategory(Faq.FaqCategory category) {
        List<Faq> faqs = faqRepository.findByIsActiveTrueAndCategoryOrderByDisplayOrderAscCreatedAtDesc(category);
        
        return faqs.stream()
                .map(FaqDto.FaqResponse::from)
                .collect(Collectors.toList());
    }

    /**
     * FAQ 총 개수 조회
     */
    public long getTotalFaqCount() {
        return faqRepository.countByIsActiveTrue();
    }
}
