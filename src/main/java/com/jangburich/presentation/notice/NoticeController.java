package com.jangburich.presentation.notice;

import com.jangburich.application.notice.NoticeService;
import com.jangburich.global.payload.ApiResponse;
import com.jangburich.global.payload.BaseResponse;
import com.jangburich.global.payload.NoticeDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/notices")
@RequiredArgsConstructor
@Tag(name = "Notice", description = "공지사항 API")
public class NoticeController {

    private final NoticeService noticeService;

    @GetMapping
    @Operation(summary = "공지사항 목록 조회", description = "공지사항 목록을 페이징으로 조회합니다.")
    public ResponseEntity<?> getNotices(
            @Parameter(description = "페이지 번호 (0부터 시작)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "페이지 크기", example = "10")
            @RequestParam(defaultValue = "10") int size) {
        
        NoticeDto.NoticePageResponse response = noticeService.getNotices(page, size);
        return ResponseEntity.ok(new BaseResponse<>(response, LocalDateTime.now(), "OK"));
    }

    @GetMapping("/{noticeId}")
    @Operation(summary = "공지사항 상세 조회", description = "특정 공지사항의 상세 정보를 조회합니다.")
    public ResponseEntity<?> getNoticeDetail(
            @Parameter(description = "공지사항 ID", example = "1")
            @PathVariable Long noticeId) {
        
        NoticeDto.NoticeDetailResponse response = noticeService.getNoticeDetail(noticeId);
        return ResponseEntity.ok(new BaseResponse<>(response, LocalDateTime.now(), "OK"));
    }

    @GetMapping("/important")
    @Operation(summary = "중요 공지사항 조회", description = "중요 공지사항 목록을 조회합니다.")
    public ResponseEntity<?> getImportantNotices() {
        List<NoticeDto.NoticeListResponse> response = noticeService.getImportantNotices();

        return ResponseEntity.ok(new BaseResponse<>(response, LocalDateTime.now(), "OK"));
    }

    @GetMapping("/count")
    @Operation(summary = "공지사항 총 개수 조회", description = "활성화된 공지사항의 총 개수를 조회합니다.")
    public ResponseEntity<?> getTotalNoticeCount() {
        long count = noticeService.getTotalNoticeCount();

        return ResponseEntity.ok(new BaseResponse<>(count, LocalDateTime.now(), "OK"));
    }
}
