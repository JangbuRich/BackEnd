package com.jangburich.domain.owner.domain.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.jangburich.domain.owner.domain.controller.dto.req.OwnerCreateReqDTO;
import com.jangburich.domain.owner.domain.service.OwnerCommandService;
import com.jangburich.global.payload.Message;
import com.jangburich.global.payload.ResponseCustom;
import com.jangburich.utils.parser.AuthenticationParser;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Owner", description = "Owner API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/owner")
public class OwnerCommandController {

    private final OwnerCommandService ownerCommandService;

    @Operation(summary = "사장님 회원가입", description = "사장님 회원가입 진행.")
    @PostMapping("/register")
    public ResponseCustom<Message> registerOwner (Authentication authentication,
        @RequestBody OwnerCreateReqDTO ownerCreateReqDTO) {

        ownerCommandService.registerOwner(AuthenticationParser.parseUserId(authentication), ownerCreateReqDTO);
        return ResponseCustom.OK(Message.builder()
            .message("success")
            .build());
    }

}
