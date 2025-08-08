package com.jangburich.presentation.terms;

import com.jangburich.application.terms.TermsService;
import com.jangburich.domain.entity.Terms;
import com.jangburich.global.payload.ApiResponse;
import com.jangburich.global.payload.BaseResponse;
import com.jangburich.global.payload.TermsDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/terms")
@RequiredArgsConstructor
@Tag(name = "Terms", description = "이용약관 API")
public class TermsController {

    private final TermsService termsService;

    @GetMapping
    @Operation(summary = "모든 이용약관 조회", description = "모든 이용약관 목록을 조회합니다.")
    public ResponseEntity<?> getAllTerms() {
        TermsDto.TermsListResponse response = termsService.getAllTerms();

        return ResponseEntity.ok(new BaseResponse<>(response, LocalDateTime.now(), "OK"));
    }

    @GetMapping("/type/{type}")
    @Operation(summary = "타입별 최신 이용약관 조회", description = "특정 타입의 최신 이용약관을 조회합니다.")
    public ResponseEntity<?> getTermsByType(
            @Parameter(description = "약관 타입", example = "SERVICE")
            @PathVariable Terms.TermsType type) {
        
        TermsDto.TermsResponse response = termsService.getTermsByType(type);
        return ResponseEntity.ok(new BaseResponse<>(response, LocalDateTime.now(), "OK"));
    }

    @GetMapping("/{termsId}")
    @Operation(summary = "이용약관 상세 조회", description = "특정 이용약관의 상세 정보를 조회합니다.")
    public ResponseEntity<?> getTermsDetail(
            @Parameter(description = "약관 ID", example = "1")
            @PathVariable Long termsId) {
        
        TermsDto.TermsResponse response = termsService.getTermsDetail(termsId);
        return ResponseEntity.ok(new BaseResponse<>(response, LocalDateTime.now(), "OK"));
    }

    @GetMapping("/count")
    @Operation(summary = "이용약관 총 개수 조회", description = "활성화된 이용약관의 총 개수를 조회합니다.")
    public ResponseEntity<?> getTotalTermsCount() {
        long count = termsService.getTotalTermsCount();

        return ResponseEntity.ok(new BaseResponse<>(count, LocalDateTime.now(), "OK"));
    }
}
