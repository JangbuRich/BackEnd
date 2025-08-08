package com.jangburich.infrastructure.repository;

import com.jangburich.domain.entity.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Long> {
    
    // 활성화된 공지사항만 조회 (페이징)
    Page<Notice> findByIsActiveTrueOrderByIsImportantDescCreatedAtDesc(Pageable pageable);
    
    // 중요 공지사항 조회
    List<Notice> findByIsActiveTrueAndIsImportantTrueOrderByCreatedAtDesc();
    
    // 활성화된 공지사항 개수
    long countByIsActiveTrue();
}
