package com.jangburich.domain.user.controller;

import com.jangburich.domain.user.domain.User;
import com.jangburich.domain.user.dto.response.UserHomeResponse;
import com.jangburich.domain.user.dto.response.WalletResponse;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.jangburich.domain.user.domain.AdditionalInfoCreateDTO;
import com.jangburich.domain.user.domain.KakaoApiResponseDTO;
import com.jangburich.domain.user.domain.TokenResponseDTO;
import com.jangburich.domain.user.service.UserService;
import com.jangburich.global.payload.Message;
import com.jangburich.global.payload.ResponseCustom;
import com.jangburich.utils.parser.AuthenticationParser;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@PostMapping("/login")
	public ResponseCustom<TokenResponseDTO> login(@RequestParam String authorizationHeader) {
		TokenResponseDTO login = userService.login(authorizationHeader);
		return ResponseCustom.OK(login);
	}

	@GetMapping("/user-info")
	public ResponseEntity<User> getUserInfo(Authentication authentication) {
		User userInfo = userService.getUserInfos(AuthenticationParser.parseUserId(authentication));
		return ResponseEntity.ok(userInfo);
	}

	@PostMapping("/join/user")
	public ResponseCustom<TokenResponseDTO> joinUser(@RequestParam String authorizationHeader) {
		return ResponseCustom.OK(userService.joinUser(authorizationHeader));
	}

	@PostMapping("/join/owner")
	public ResponseCustom<TokenResponseDTO> joinOwner(@RequestParam String authorizationHeader) {
		return ResponseCustom.OK(userService.joinOwner(authorizationHeader));
	}

	@PostMapping("/token/reissue")
	public ResponseEntity<String> reissueAccessToken(@RequestParam String refreshToken) {
		String newAccessToken = userService.reissueAccessToken(refreshToken);
		return ResponseEntity.ok(newAccessToken);
	}

	@PostMapping("/token")
	public ResponseEntity<Map<String, Object>> getAccessToken(@RequestParam("code") String code) {
		String tokenUrl = "https://kauth.kakao.com/oauth/token";

		// 요청 헤더 설정
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		// 요청 바디 설정
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("grant_type", "authorization_code");
		params.add("client_id", "e155fb32e2e3f793d0ddd66212d8d461");
		params.add("redirect_uri", "http://localhost:5123");
		params.add("code", code);

		// 요청 생성
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
		RestTemplate restTemplate = new RestTemplate();

		// 요청 보내기 및 응답 받기
		ResponseEntity<Map> response = restTemplate.exchange(tokenUrl, HttpMethod.POST, request, Map.class);

		// 응답 반환
		return ResponseEntity.ok(response.getBody());
	}

	@Operation(summary = "유저 추가정보 저장", description = "유저 추가정보 저장합니다.")
	@PostMapping("/additionalInfo")
	public ResponseCustom<?> saveAdditionalInfo(Authentication authentication,
		@RequestBody AdditionalInfoCreateDTO additionalInfoCreateDTO) {
		userService.additionalInfo(AuthenticationParser.parseUserId(authentication), additionalInfoCreateDTO);
		return ResponseCustom.OK(Message.builder().message("success").build());
	}

	@Operation(summary = "나의 지갑 조회", description = "나의 현재 남아 있는 포인트를 조회합니다.")
	@GetMapping("/wallet")
	public ResponseCustom<WalletResponse> getMyWallet(Authentication authentication) {
		return ResponseCustom.OK(userService.getMyWallet(AuthenticationParser.parseUserId(authentication)));
	}

	@Operation(summary = "유저 홈 화면 조회", description = "유저의 홈 화면을 조회합니다.")
	@GetMapping("/home")
	public ResponseCustom<UserHomeResponse> getUserHome(Authentication authentication) {
		return ResponseCustom.OK(userService.getUserHome(AuthenticationParser.parseUserId(authentication)));
	}
}