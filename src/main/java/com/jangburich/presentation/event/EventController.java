package com.jangburich.presentation.event;

import com.jangburich.application.event.EventService;
import com.jangburich.global.payload.ApiResponse;
import com.jangburich.global.payload.BaseResponse;
import com.jangburich.global.payload.EventDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
@Tag(name = "Event", description = "이벤트 API")
public class EventController {

    private final EventService eventService;

    @GetMapping
    @Operation(summary = "이벤트 목록 조회", description = "이벤트 목록을 페이징으로 조회합니다.")
    public ResponseEntity<?> getEvents(
            @Parameter(description = "페이지 번호 (0부터 시작)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "페이지 크기", example = "10")
            @RequestParam(defaultValue = "10") int size) {
        
        EventDto.EventPageResponse response = eventService.getEvents(page, size);

        return ResponseEntity.ok(new BaseResponse<>(response, LocalDateTime.now(), "OK"));
    }

    @GetMapping("/{eventId}")
    @Operation(summary = "이벤트 상세 조회", description = "특정 이벤트의 상세 정보를 조회합니다.")
    public ResponseEntity<?> getEventDetail(
            @Parameter(description = "이벤트 ID", example = "1")
            @PathVariable Long eventId) {
        
        EventDto.EventDetailResponse response = eventService.getEventDetail(eventId);
        return ResponseEntity.ok(new BaseResponse<>(response, LocalDateTime.now(), "OK"));
    }

    @GetMapping("/ongoing")
    @Operation(summary = "진행중인 이벤트 조회", description = "현재 진행중인 이벤트 목록을 조회합니다.")
    public ResponseEntity<?> getOngoingEvents() {
        List<EventDto.EventListResponse> response = eventService.getOngoingEvents();

        return ResponseEntity.ok(new BaseResponse<>(response, LocalDateTime.now(), "OK"));
    }

    @GetMapping("/upcoming")
    @Operation(summary = "예정된 이벤트 조회", description = "예정된 이벤트 목록을 조회합니다.")
    public ResponseEntity<?> getUpcomingEvents() {
        List<EventDto.EventListResponse> response = eventService.getUpcomingEvents();

        return ResponseEntity.ok(new BaseResponse<>(response, LocalDateTime.now(), "OK"));
    }

    @GetMapping("/count")
    @Operation(summary = "이벤트 총 개수 조회", description = "활성화된 이벤트의 총 개수를 조회합니다.")
    public ResponseEntity<?> getTotalEventCount() {
        long count = eventService.getTotalEventCount();

        return ResponseEntity.ok(new BaseResponse<>(count, LocalDateTime.now(), "OK"));
    }
}
