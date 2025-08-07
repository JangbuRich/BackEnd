package com.jangburich.application.notice;

import com.jangburich.domain.entity.Notice;
import com.jangburich.global.payload.NoticeDto;
import com.jangburich.infrastructure.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NoticeService {

    private final NoticeRepository noticeRepository;

    /**
     * 공지사항 목록 조회 (페이징)
     */
    public NoticeDto.NoticePageResponse getNotices(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Notice> noticePage = noticeRepository.findByIsActiveTrueOrderByIsImportantDescCreatedAtDesc(pageable);

        List<NoticeDto.NoticeListResponse> notices = noticePage.getContent().stream()
                .map(NoticeDto.NoticeListResponse::from)
                .collect(Collectors.toList());

        return NoticeDto.NoticePageResponse.builder()
                .notices(notices)
                .currentPage(noticePage.getNumber())
                .totalPages(noticePage.getTotalPages())
                .totalElements(noticePage.getTotalElements())
                .hasNext(noticePage.hasNext())
                .hasPrevious(noticePage.hasPrevious())
                .build();
    }

    /**
     * 공지사항 상세 조회
     */
    public NoticeDto.NoticeDetailResponse getNoticeDetail(Long noticeId) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new IllegalArgumentException("공지사항을 찾을 수 없습니다."));

        if (!notice.getIsActive()) {
            throw new IllegalArgumentException("비활성화된 공지사항입니다.");
        }

        return NoticeDto.NoticeDetailResponse.from(notice);
    }

    /**
     * 중요 공지사항 조회
     */
    public List<NoticeDto.NoticeListResponse> getImportantNotices() {
        List<Notice> importantNotices = noticeRepository.findByIsActiveTrueAndIsImportantTrueOrderByCreatedAtDesc();
        
        return importantNotices.stream()
                .map(NoticeDto.NoticeListResponse::from)
                .collect(Collectors.toList());
    }

    /**
     * 공지사항 총 개수 조회
     */
    public long getTotalNoticeCount() {
        return noticeRepository.countByIsActiveTrue();
    }
}
