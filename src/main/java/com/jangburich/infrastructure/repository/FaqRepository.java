package com.jangburich.infrastructure.repository;

import com.jangburich.domain.entity.Faq;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FaqRepository extends JpaRepository<Faq, Long> {
    
    // 활성화된 FAQ 조회 (카테고리별, 표시순서별)
    List<Faq> findByIsActiveTrueOrderByCategoryAscDisplayOrderAscCreatedAtDesc();
    
    // 카테고리별 FAQ 조회
    List<Faq> findByIsActiveTrueAndCategoryOrderByDisplayOrderAscCreatedAtDesc(Faq.FaqCategory category);
    
    // 활성화된 FAQ 개수
    long countByIsActiveTrue();
}
