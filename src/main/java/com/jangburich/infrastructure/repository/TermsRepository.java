package com.jangburich.infrastructure.repository;

import com.jangburich.domain.entity.Terms;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TermsRepository extends JpaRepository<Terms, Long> {
    
    // 활성화된 약관 조회
    List<Terms> findByIsActiveTrueOrderByTypeAscCreatedAtDesc();
    
    // 타입별 최신 약관 조회
    Optional<Terms> findFirstByIsActiveTrueAndTypeOrderByCreatedAtDesc(Terms.TermsType type);
    
    // 활성화된 약관 개수
    long countByIsActiveTrue();
}
