package com.jangburich.infrastructure.repository;

import com.jangburich.domain.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    
    // 활성화된 이벤트만 조회 (페이징)
    Page<Event> findByIsActiveTrueOrderByCreatedAtDesc(Pageable pageable);
    
    // 진행중인 이벤트 조회
    @Query("SELECT e FROM Event e WHERE e.isActive = true AND e.startDate <= :now AND e.endDate >= :now ORDER BY e.createdAt DESC")
    List<Event> findOngoingEvents(LocalDateTime now);
    
    // 예정된 이벤트 조회
    @Query("SELECT e FROM Event e WHERE e.isActive = true AND e.startDate > :now ORDER BY e.startDate ASC")
    List<Event> findUpcomingEvents(LocalDateTime now);
    
    // 활성화된 이벤트 개수
    long countByIsActiveTrue();
}
