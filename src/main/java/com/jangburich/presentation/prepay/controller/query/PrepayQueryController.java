package com.jangburich.presentation.prepay.controller.query;

import java.time.LocalDateTime;
import java.util.List;

import com.jangburich.application.prepay.service.PrepayQueryService;
import com.jangburich.global.payload.BaseResponse;
import com.jangburich.global.payload.ResponseCustom;
import com.jangburich.presentation.prepay.dto.response.PrepayResponse;
import com.jangburich.utils.parser.AuthenticationParser;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Prepay", description = "Prepay Query API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/prepay")
public class PrepayQueryController {

    private final PrepayQueryService prepayQueryService;

    @Operation(summary = "선결제 정보 조회", description = "선결제 진행하기 위한 정보를 조회합니다.")
    @GetMapping
    public ResponseCustom<?> getPrepayInfo (Authentication authentication,
        @RequestParam(value = "storeId") Long storeId,
        @RequestParam(value = "teamId") Long teamId
    ) {
        return ResponseCustom.OK(
            prepayQueryService.getPrepayInfo(AuthenticationParser.parseUserId(authentication), storeId, teamId));
    }

    @Operation(summary = "가게 선결제 내역 조회", description = "해당 가게에 선결제된 결제 정보를 조회한다.")
    @GetMapping("/store")
    public ResponseEntity<?> getStorePrepay (Authentication authentication) {
        List<PrepayResponse.StorePrepayInfo> storePrepayInfo = prepayQueryService.getStorePrepayInfo(
            AuthenticationParser.parseUserId(authentication));

        return ResponseEntity.ok(new BaseResponse<>(storePrepayInfo, LocalDateTime.now(), "OK"));
    }

    @Operation(summary = "그룹원, 그릅원명으로 검색을 진행한다.", description = "그룹원, 그룹원명을 통해 결제 정보를 조회한다.")
    @GetMapping("/search")
    public ResponseEntity<?> searchStorePrepay (Authentication authentication,
        @RequestParam(value = "name") String name) {
        List<PrepayResponse.StorePrepayInfo> searchStorePrepayInfoList = prepayQueryService.getSearchStorePrepayInfoByName(
            AuthenticationParser.parseUserId(authentication), name);

        return ResponseEntity.ok(new BaseResponse<>(searchStorePrepayInfoList, LocalDateTime.now(), "OK"));
    }

    @Operation(summary = "날짜 기반으로 필터링을 진행한다.", description = "시작날짜 - 종료날짜 기준으로 조회한다.")
    @GetMapping("/filter")
    public ResponseEntity<?> filterDateStorePrepay(
        Authentication authentication,
        @RequestParam(value = "startDate") @NotBlank(message = "startDate must not be empty") String startDate,
        @RequestParam(value = "endDate") @NotBlank(message = "endDate must not be empty") String endDate) {

        List<PrepayResponse.StorePrepayInfo> prepayInfoFilterByDate = prepayQueryService.getPrepayInfoFilterByDate(
            AuthenticationParser.parseUserId(authentication), startDate, endDate);

        return ResponseEntity.ok(new BaseResponse<>(prepayInfoFilterByDate, LocalDateTime.now(), "OK"));
    }

}
