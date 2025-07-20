package com.jangburich.presentation.prepay.controller.query;

import com.jangburich.application.prepay.service.PrepaySearchQueryService;
import com.jangburich.global.payload.BaseResponse;
import com.jangburich.presentation.prepay.dto.response.PrepayResponse;
import com.jangburich.utils.parser.AuthenticationParser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@Tag(name = "Prepay", description = "Prepay Query API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/prepay")
public class PrepaySearchQueryController {

    private final PrepaySearchQueryService prepaySearchQueryService;

    @Operation(summary = "그룹원, 그릅원명으로 검색을 진행한다.", description = "그룹원, 그룹원명을 통해 결제 정보를 조회한다.")
    @GetMapping("/search")
    public ResponseEntity<?> searchStorePrepay (Authentication authentication,
                                                @RequestParam(value = "name") String name) {
        List<PrepayResponse.StorePrepayInfo> searchStorePrepayInfoList = prepaySearchQueryService.getSearchStorePrepayInfoByName(
                AuthenticationParser.parseUserId(authentication), name);

        return ResponseEntity.ok(new BaseResponse<>(searchStorePrepayInfoList, LocalDateTime.now(), "OK"));
    }

    @Operation(summary = "날짜 기반으로 필터링을 진행한다.", description = "시작날짜 - 종료날짜 기준으로 조회한다.")
    @GetMapping("/filter")
    public ResponseEntity<?> filterDateStorePrepay(
            Authentication authentication,
            @RequestParam(value = "startDate") @NotBlank(message = "startDate must not be empty") String startDate,
            @RequestParam(value = "endDate") @NotBlank(message = "endDate must not be empty") String endDate) {

        List<PrepayResponse.StorePrepayInfo> prepayInfoFilterByDate = prepaySearchQueryService.getPrepayInfoFilterByDate(
                AuthenticationParser.parseUserId(authentication), startDate, endDate);

        return ResponseEntity.ok(new BaseResponse<>(prepayInfoFilterByDate, LocalDateTime.now(), "OK"));
    }

}
