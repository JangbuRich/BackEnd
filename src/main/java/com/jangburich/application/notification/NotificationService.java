package com.jangburich.application.notification;

import com.jangburich.domain.entity.NotificationSetting;
import com.jangburich.domain.user.domain.User;
import com.jangburich.global.payload.NotificationDto;
import com.jangburich.infrastructure.repository.NotificationSettingRepository;
import com.jangburich.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationService {

    private final NotificationSettingRepository notificationSettingRepository;
    private final UserRepository userRepository;

    /**
     * 사용자 알림설정 조회
     */
    public NotificationDto.NotificationSettingResponse getNotificationSetting(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        NotificationSetting setting = notificationSettingRepository.findByUser(user)
                .orElseGet(() -> createDefaultNotificationSetting(user));

        return NotificationDto.NotificationSettingResponse.from(setting);
    }

    /**
     * 사용자 알림설정 업데이트
     */
    @Transactional
    public NotificationDto.NotificationSettingResponse updateNotificationSetting(
            Long userId, NotificationDto.NotificationSettingRequest request) {
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        NotificationSetting setting = notificationSettingRepository.findByUser(user)
                .orElseGet(() -> createDefaultNotificationSetting(user));

        setting.updateSettings(
                request.getPushNotificationEnabled(),
                request.getOrderNotificationEnabled(),
                request.getPaymentNotificationEnabled(),
                request.getEventNotificationEnabled(),
                request.getMarketingNotificationEnabled()
        );

        NotificationSetting savedSetting = notificationSettingRepository.save(setting);
        return NotificationDto.NotificationSettingResponse.from(savedSetting);
    }

    /**
     * 기본 알림설정 생성
     */
    @Transactional
    public NotificationSetting createDefaultNotificationSetting(User user) {
        NotificationSetting defaultSetting = NotificationSetting.builder()
                .user(user)
                .pushNotificationEnabled(true)
                .orderNotificationEnabled(true)
                .paymentNotificationEnabled(true)
                .eventNotificationEnabled(true)
                .marketingNotificationEnabled(false)
                .build();

        return notificationSettingRepository.save(defaultSetting);
    }
}
