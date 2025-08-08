package com.jangburich.global.payload;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

public class MyPageDto {

    @Getter
    @Builder
    public static class MyPageResponse {
        private UserInfo userInfo;
        private List<NoticeDto.NoticeListResponse> recentNotices;
        private List<EventDto.EventListResponse> ongoingEvents;
        private NotificationDto.NotificationSettingResponse notificationSettings;
        private long totalNoticeCount;
        private long totalEventCount;
        private long totalFaqCount;
    }

    @Getter
    @Builder
    public static class UserInfo {
        private Long id;
        private String email;
        private String phoneNumber;
        private String businessNumber;
        private String name;
    }
}
