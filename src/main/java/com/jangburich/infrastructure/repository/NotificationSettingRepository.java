package com.jangburich.infrastructure.repository;

import com.jangburich.domain.entity.NotificationSetting;
import com.jangburich.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NotificationSettingRepository extends JpaRepository<NotificationSetting, Long> {
    
    // 사용자별 알림설정 조회
    Optional<NotificationSetting> findByUser(User user);
    
    // 사용자 ID로 알림설정 조회
    // Optional<NotificationSetting> findByUserId(Long userId);
}
