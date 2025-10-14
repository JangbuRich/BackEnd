package com.jangburich.presentation.owner.controller;

import com.jangburich.application.owner.service.command.OwnerCommandService;
import com.jangburich.global.payload.BaseResponse;
import com.jangburich.global.payload.Message;
import com.jangburich.global.payload.ResponseCustom;
import com.jangburich.presentation.owner.controller.dto.req.OwnerCreateReqDTO;
import com.jangburich.utils.parser.AuthenticationParser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@Tag(name = "Owner", description = "Owner API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/owner")
public class OwnerCommandController {

    private final OwnerCommandService ownerCommandService;

    @Operation(summary = "사장님 회원가입", description = "사장님 회원가입 진행.")
    @PostMapping("/register")
    public ResponseEntity<?> registerOwner(Authentication authentication, @RequestBody OwnerCreateReqDTO ownerCreateReqDTO) {
        ownerCommandService.registerOwner(AuthenticationParser.parseUserId(authentication), ownerCreateReqDTO);

        return ResponseEntity.ok(new BaseResponse<String>("success", LocalDateTime.now(), "OK"));
    }

}
