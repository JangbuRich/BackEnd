package com.jangburich.domain.owner.domain.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jangburich.domain.owner.domain.OwnerCreateReqDTO;
import com.jangburich.domain.owner.domain.OwnerGetResDTO;
import com.jangburich.domain.owner.domain.service.OwnerService;
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
public class OwnerController {

	private final OwnerService ownerService;

	@Operation(summary = "사장님 정보 등록", description = "사장님 상세 정보를 등록합니다.")
	@PostMapping("/register")
	public ResponseCustom<Message> registerOwner(
		Authentication authentication,
		OwnerCreateReqDTO ownerCreateReqDTO) {
		ownerService.registerOwner(AuthenticationParser.parseUserId(authentication), ownerCreateReqDTO);
		return ResponseCustom.OK(Message.builder()
			.message("success")
			.build());
	}

	@Operation(summary = "사장님 정보 조회", description = "사장님 정보를 조회합니다.")
	@GetMapping("")
	public ResponseCustom<OwnerGetResDTO> getOwnerInfo(
		Authentication authentication) {
		return ResponseCustom.OK(ownerService.getOwnerInfo(AuthenticationParser.parseUserId(authentication)));
	}
}
