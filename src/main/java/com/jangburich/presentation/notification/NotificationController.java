package com.jangburich.presentation.notification;

import com.jangburich.application.notification.NotificationService;
import com.jangburich.global.payload.ApiResponse;
import com.jangburich.global.payload.BaseResponse;
import com.jangburich.global.payload.NotificationDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Tag(name = "Notification", description = "알림설정 API")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/settings/{userId}")
    @Operation(summary = "알림설정 조회", description = "사용자의 알림설정을 조회합니다.")
    public ResponseEntity<?> getNotificationSetting(
            @Parameter(description = "사용자 ID", example = "1")
            @PathVariable Long userId) {
        
        NotificationDto.NotificationSettingResponse response = notificationService.getNotificationSetting(userId);
        return ResponseEntity.ok(new BaseResponse<>(response, LocalDateTime.now(), "OK"));
    }

    @PutMapping("/settings/{userId}")
    @Operation(summary = "알림설정 업데이트", description = "사용자의 알림설정을 업데이트합니다.")
    public ResponseEntity<?> updateNotificationSetting(
            @Parameter(description = "사용자 ID", example = "1")
            @PathVariable Long userId,
            @RequestBody NotificationDto.NotificationSettingRequest request) {
        
        NotificationDto.NotificationSettingResponse response = 
                notificationService.updateNotificationSetting(userId, request);
        return ResponseEntity.ok(new BaseResponse<>(response, LocalDateTime.now(), "OK"));
    }
}
