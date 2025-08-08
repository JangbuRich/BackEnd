package com.jangburich.global.payload;

import com.jangburich.domain.entity.NotificationSetting;
import lombok.Builder;
import lombok.Getter;

public class NotificationDto {

    @Getter
    @Builder
    public static class NotificationSettingResponse {
        private Boolean pushNotificationEnabled;
        private Boolean orderNotificationEnabled;
        private Boolean paymentNotificationEnabled;
        private Boolean eventNotificationEnabled;
        private Boolean marketingNotificationEnabled;

        public static NotificationSettingResponse from(NotificationSetting setting) {
            return NotificationSettingResponse.builder()
                    .pushNotificationEnabled(setting.getPushNotificationEnabled())
                    .orderNotificationEnabled(setting.getOrderNotificationEnabled())
                    .paymentNotificationEnabled(setting.getPaymentNotificationEnabled())
                    .eventNotificationEnabled(setting.getEventNotificationEnabled())
                    .marketingNotificationEnabled(setting.getMarketingNotificationEnabled())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class NotificationSettingRequest {
        private Boolean pushNotificationEnabled;
        private Boolean orderNotificationEnabled;
        private Boolean paymentNotificationEnabled;
        private Boolean eventNotificationEnabled;
        private Boolean marketingNotificationEnabled;
    }
}
