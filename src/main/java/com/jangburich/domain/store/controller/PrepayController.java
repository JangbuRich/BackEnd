package com.jangburich.domain.store.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jangburich.domain.store.dto.request.PrepayRequest;
import com.jangburich.domain.store.service.PrepayService;
import com.jangburich.global.payload.Message;
import com.jangburich.global.payload.ResponseCustom;
import com.jangburich.utils.parser.AuthenticationParser;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Prepay", description = "Prepay API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/prepay")
public class PrepayController {

	private final PrepayService prepayService;

	@Operation(summary = "선결제", description = "팀과 매장 선결제를 진행합니다.")
	@PostMapping
	public ResponseCustom<Message> prepay(Authentication authentication,
		@RequestBody PrepayRequest prepayRequest) {
		return ResponseCustom.OK(prepayService.prepay(AuthenticationParser.parseUserId(authentication), prepayRequest));
	}

	@Operation(summary = "선결제 정보 조회", description = "선결제 진행하기 위한 정보를 조회합니다.")
	@GetMapping("")
	public ResponseCustom<?> getPrepayInfo(Authentication authentication,
		@RequestParam(value = "storeId") Long storeId,
		@RequestParam(value = "teamId") Long teamId
	) {
		return ResponseCustom.OK(
			prepayService.getPrepayInfo(AuthenticationParser.parseUserId(authentication), storeId, teamId));
	}
}
