package com.jangburich.application.event;

import com.jangburich.domain.entity.Event;
import com.jangburich.global.payload.EventDto;
import com.jangburich.infrastructure.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventService {

    private final EventRepository eventRepository;

    /**
     * 이벤트 목록 조회 (페이징)
     */
    public EventDto.EventPageResponse getEvents(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Event> eventPage = eventRepository.findByIsActiveTrueOrderByCreatedAtDesc(pageable);

        List<EventDto.EventListResponse> events = eventPage.getContent().stream()
                .map(EventDto.EventListResponse::from)
                .collect(Collectors.toList());

        return EventDto.EventPageResponse.builder()
                .events(events)
                .currentPage(eventPage.getNumber())
                .totalPages(eventPage.getTotalPages())
                .totalElements(eventPage.getTotalElements())
                .hasNext(eventPage.hasNext())
                .hasPrevious(eventPage.hasPrevious())
                .build();
    }

    /**
     * 이벤트 상세 조회
     */
    public EventDto.EventDetailResponse getEventDetail(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("이벤트를 찾을 수 없습니다."));

        if (!event.getIsActive()) {
            throw new IllegalArgumentException("비활성화된 이벤트입니다.");
        }

        return EventDto.EventDetailResponse.from(event);
    }

    /**
     * 진행중인 이벤트 조회
     */
    public List<EventDto.EventListResponse> getOngoingEvents() {
        List<Event> ongoingEvents = eventRepository.findOngoingEvents(LocalDateTime.now());
        
        return ongoingEvents.stream()
                .map(EventDto.EventListResponse::from)
                .collect(Collectors.toList());
    }

    /**
     * 예정된 이벤트 조회
     */
    public List<EventDto.EventListResponse> getUpcomingEvents() {
        List<Event> upcomingEvents = eventRepository.findUpcomingEvents(LocalDateTime.now());
        
        return upcomingEvents.stream()
                .map(EventDto.EventListResponse::from)
                .collect(Collectors.toList());
    }

    /**
     * 이벤트 총 개수 조회
     */
    public long getTotalEventCount() {
        return eventRepository.countByIsActiveTrue();
    }
}
