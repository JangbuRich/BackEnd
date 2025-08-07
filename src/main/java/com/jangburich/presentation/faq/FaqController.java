package com.jangburich.presentation.faq;

import com.jangburich.application.faq.FaqService;
import com.jangburich.domain.entity.Faq;
import com.jangburich.global.payload.BaseResponse;
import com.jangburich.global.payload.FaqDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/faqs")
@RequiredArgsConstructor
@Tag(name = "FAQ", description = "자주 묻는 질문 API")
public class FaqController {

    private final FaqService faqService;

    @GetMapping
    @Operation(summary = "FAQ 목록 조회", description = "카테고리별로 그룹화된 FAQ 목록을 조회합니다.")
    public ResponseEntity<?> getFaqs() {
        FaqDto.FaqListResponse response = faqService.getFaqs();
        return ResponseEntity.ok(new BaseResponse<>(response, LocalDateTime.now(), "OK"));
    }

    @GetMapping("/category/{category}")
    @Operation(summary = "카테고리별 FAQ 조회", description = "특정 카테고리의 FAQ 목록을 조회합니다.")
    public ResponseEntity<?> getFaqsByCategory(
            @Parameter(description = "FAQ 카테고리", example = "GENERAL")
            @PathVariable Faq.FaqCategory category) {
        
        List<FaqDto.FaqResponse> response = faqService.getFaqsByCategory(category);
        return ResponseEntity.ok(new BaseResponse<>(response, LocalDateTime.now(), "OK"));
    }

    @GetMapping("/count")
    @Operation(summary = "FAQ 총 개수 조회", description = "활성화된 FAQ의 총 개수를 조회합니다.")
    public ResponseEntity<?> getTotalFaqCount() {
        long count = faqService.getTotalFaqCount();

        return ResponseEntity.ok(new BaseResponse<>(count, LocalDateTime.now(), "OK"));
    }
}
