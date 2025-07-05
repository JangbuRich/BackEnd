package com.jangburich.domain.owner.domain.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jangburich.domain.owner.domain.controller.dto.res.OwnerGetResDTO;
import com.jangburich.domain.owner.domain.service.OwnerQueryService;
import com.jangburich.global.error.DefaultNullPointerException;
import com.jangburich.global.payload.ErrorCode;
import com.jangburich.global.payload.ResponseCustom;
import com.jangburich.utils.parser.AuthenticationParser;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Tag(name = "Owner", description = "Owner API")
@RequestMapping("/api/v1/owner")
@RestController
public class OwnerQueryController {
    private final OwnerQueryService ownerQueryService;

    @Operation(summary = "사업자등록번호 검증", description = "사업자 등록번호 검증을 진행한다. ")
    @GetMapping("/business-no/validate")
    public ResponseCustom<?> getValidateBusinessNo(@RequestParam String businessNo) {
        if(businessNo == null || businessNo.isEmpty())
            throw new DefaultNullPointerException(ErrorCode.INVALID_OPTIONAL_ISPRESENT);

        return ResponseCustom.OK(ownerQueryService.getValidateBusinessNo(businessNo));
    }

    @Operation(summary = "계좌번호 검증", description = "계좌 번호를 검증을 진행한다")
    @GetMapping("/account-no/validate")
    public ResponseCustom<?> getValidateAccountNo(
        @RequestParam String account,
        @RequestParam String accountNo) {
        return ResponseCustom.OK(ownerQueryService.getValidateAccountNo(account, accountNo));
    }

    @Operation(summary = "사장님 정보 조회", description = "사장님 정보를 조회한다.")
    @GetMapping
    public ResponseCustom<OwnerGetResDTO> getOwnerInfo(
        Authentication authentication) {
        return ResponseCustom.OK(ownerQueryService.getOwnerInfo(AuthenticationParser.parseUserId(authentication)));
    }

}
