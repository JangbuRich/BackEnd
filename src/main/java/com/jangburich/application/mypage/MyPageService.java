package com.jangburich.application.mypage;

import com.jangburich.application.event.EventService;
import com.jangburich.application.faq.FaqService;
import com.jangburich.application.notice.NoticeService;
import com.jangburich.application.notification.NotificationService;
import com.jangburich.application.store.resolver.StoreResolver;
import com.jangburich.application.store.resolver.context.StoreContext;
import com.jangburich.domain.owner.Owner;
import com.jangburich.domain.user.domain.User;
import com.jangburich.global.payload.EventDto;
import com.jangburich.global.payload.MyPageDto;
import com.jangburich.global.payload.NoticeDto;
import com.jangburich.global.payload.NotificationDto;
import com.jangburich.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MyPageService {

    private final UserRepository userRepository;
    private final NoticeService noticeService;
    private final EventService eventService;
    private final FaqService faqService;
    private final NotificationService notificationService;
    private final StoreResolver storeResolver;

    /**
     * 마이페이지 정보 조회
     */
    public MyPageDto.MyPageResponse getMyPageInfo(Long userId) {
        StoreContext storeContext = storeResolver.getStoreContext(String.valueOf(userId));
        User user = storeContext.getUser();
        Owner owner = storeContext.getOwner();

        // 사용자 정보
        MyPageDto.UserInfo userInfo = MyPageDto.UserInfo.builder()
                .id(user.getUserId())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .businessNumber(owner.getBusinessRegistrationNumber())
                .name(user.getName())
                .build();

        // 최근 공지사항 (최대 3개)
        NoticeDto.NoticePageResponse recentNoticesPage = noticeService.getNotices(0, 3);
        List<NoticeDto.NoticeListResponse> recentNotices = recentNoticesPage.getNotices();

        // 진행중인 이벤트
        List<EventDto.EventListResponse> ongoingEvents = eventService.getOngoingEvents();

        // 알림설정
        NotificationDto.NotificationSettingResponse notificationSettings = 
                notificationService.getNotificationSetting(userId);

        // 각종 카운트
        long totalNoticeCount = noticeService.getTotalNoticeCount();
        long totalEventCount = eventService.getTotalEventCount();
        long totalFaqCount = faqService.getTotalFaqCount();

        return MyPageDto.MyPageResponse.builder()
                .userInfo(userInfo)
                .recentNotices(recentNotices)
                .ongoingEvents(ongoingEvents)
                .notificationSettings(notificationSettings)
                .totalNoticeCount(totalNoticeCount)
                .totalEventCount(totalEventCount)
                .totalFaqCount(totalFaqCount)
                .build();
    }
}
