package com.jangburich.domain.owner.domain.service.dtos;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
public final class BusinessNoDto {

    @Builder
    @Getter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BusinessNoRequestDto {

        @JsonProperty("b_no")
        @NotEmpty(message = "사업자등록번호는 필수입니다")
        @Size(min = 1, max = 100, message = "사업자등록번호는 1개 이상 100개 이하로 입력해주세요")
        private List<@Pattern(regexp = "^[0-9]{10}$", message = "사업자등록번호는 10자리 숫자여야 합니다") String> businessNumbers;

        public static BusinessNoRequestDto of(String businessNumber) {
            return BusinessNoRequestDto.builder()
                .businessNumbers(List.of(businessNumber))
                .build();
        }
    }

    @Builder
    @Getter
    @ToString
    public static class BusinessNoResponseDto {
        /**
         * 응답 상태 코드
         */
        @JsonProperty("status_code")
        private String statusCode;

        /**
         * 매칭된 건수
         */
        @JsonProperty("match_cnt")
        private Integer matchCount;

        /**
         * 요청 건수
         */
        @JsonProperty("request_cnt")
        private Integer requestCount;

        /**
         * 사업자 정보 목록
         */
        @JsonProperty("data")
        private List<BusinessInfoDto> businessInfoList;

        @Builder
        @Getter
        @ToString
        public static class BusinessInfoDto {

            /**
             * 사업자등록번호
             */
            @JsonProperty("b_no")
            private String businessNumber;

            /**
             * 사업자 상태 (한글)
             */
            @JsonProperty("b_stt")
            private String businessStatus;

            /**
             * 사업자 상태 코드
             * 01: 계속사업자, 02: 휴업자, 03: 폐업자
             */
            @JsonProperty("b_stt_cd")
            private String businessStatusCode;

            /**
             * 과세 유형 (한글)
             */
            @JsonProperty("tax_type")
            private String taxType;

            /**
             * 과세 유형 코드
             * 01: 부가가치세 일반과세자, 02: 부가가치세 간이과세자, 03: 부가가치세 면세사업자
             */
            @JsonProperty("tax_type_cd")
            private String taxTypeCode;

            /**
             * 폐업일 (YYYYMMDD)
             */
            @JsonProperty("end_dt")
            private String endDate;

            /**
             * 단위과세전환사업자 여부
             * Y: 단위과세전환사업자, N: 일반사업자
             */
            @JsonProperty("utcc_yn")
            private String unitTaxConversionYn;

            /**
             * 과세유형 전환일 (YYYYMMDD)
             */
            @JsonProperty("tax_type_change_dt")
            private String taxTypeChangeDate;

            /**
             * 세금계산서 적용일 (YYYYMMDD)
             */
            @JsonProperty("invoice_apply_dt")
            private String invoiceApplyDate;

            /**
             * 직전 과세유형 (한글)
             */
            @JsonProperty("rbf_tax_type")
            private String previousTaxType;

            /**
             * 직전 과세유형 코드
             */
            @JsonProperty("rbf_tax_type_cd")
            private String previousTaxTypeCode;

            /**
             * 사업자 상태가 정상(계속사업자)인지 확인
             */
            public boolean isActiveBusiness() {
                return "01".equals(businessStatusCode);
            }

            /**
             * 부가가치세 일반과세자인지 확인
             */
            public boolean isGeneralTaxpayer() {
                return "01".equals(taxTypeCode);
            }

            /**
             * 폐업 사업자인지 확인
             */
            public boolean isClosedBusiness() {
                return "03".equals(businessStatusCode);
            }
        }
    }
}
