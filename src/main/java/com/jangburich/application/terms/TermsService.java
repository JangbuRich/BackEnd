package com.jangburich.application.terms;

import com.jangburich.domain.entity.Terms;
import com.jangburich.global.payload.TermsDto;
import com.jangburich.infrastructure.repository.TermsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TermsService {

    private final TermsRepository termsRepository;

    /**
     * 모든 이용약관 조회
     */
    public TermsDto.TermsListResponse getAllTerms() {
        List<Terms> terms = termsRepository.findByIsActiveTrueOrderByTypeAscCreatedAtDesc();

        List<TermsDto.TermsResponse> termsResponses = terms.stream()
                .map(TermsDto.TermsResponse::from)
                .collect(Collectors.toList());

        return TermsDto.TermsListResponse.builder()
                .terms(termsResponses)
                .totalCount(terms.size())
                .build();
    }

    /**
     * 특정 타입의 최신 이용약관 조회
     */
    public TermsDto.TermsResponse getTermsByType(Terms.TermsType type) {
        Terms terms = termsRepository.findFirstByIsActiveTrueAndTypeOrderByCreatedAtDesc(type)
                .orElseThrow(() -> new IllegalArgumentException("해당 타입의 이용약관을 찾을 수 없습니다."));

        return TermsDto.TermsResponse.from(terms);
    }

    /**
     * 이용약관 상세 조회
     */
    public TermsDto.TermsResponse getTermsDetail(Long termsId) {
        Terms terms = termsRepository.findById(termsId)
                .orElseThrow(() -> new IllegalArgumentException("이용약관을 찾을 수 없습니다."));

        if (!terms.getIsActive()) {
            throw new IllegalArgumentException("비활성화된 이용약관입니다.");
        }

        return TermsDto.TermsResponse.from(terms);
    }

    /**
     * 이용약관 총 개수 조회
     */
    public long getTotalTermsCount() {
        return termsRepository.countByIsActiveTrue();
    }
}
