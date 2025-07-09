package com.jangburich.domain.store.presentation.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.jangburich.domain.store.domain.Category;
import com.jangburich.domain.store.presentation.dto.request.StoreCreateRequest;
import com.jangburich.domain.store.presentation.dto.response.store.StoreCreateResponseDto;
import com.jangburich.domain.store.presentation.dto.request.StoreUpdateRequest;
import com.jangburich.domain.store.presentation.dto.response.store.SearchStoresResponse;
import com.jangburich.domain.store.service.StoreCommandService;
import com.jangburich.global.payload.Message;
import com.jangburich.global.payload.ResponseCustom;
import com.jangburich.utils.parser.AuthenticationParser;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Store", description = "Store API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/store")
public class StoreCommandController {

	private final StoreCommandService storeCommandService;

	@Operation(summary = "카테고리 별 가게 목록 조회", description = "카테고리 별로 가게 목록을 조회합니다.")
	@PostMapping("/category")
	public ResponseCustom<Page<SearchStoresResponse>> searchByCategory(
		Authentication authentication,
		@RequestParam(required = false, defaultValue = "3") Integer searchRadius,
		@RequestParam(required = false, defaultValue = "전체") String category,
		Double lat, Double lon, Pageable pageable) {
		Category categoryEnum = Category.fromDisplayName(category);
		return ResponseCustom.OK(
			storeCommandService.searchByCategory(AuthenticationParser.parseUserId(authentication), searchRadius, categoryEnum,
				lat, lon, pageable));
	}

	@Operation(summary = "매장 찾기(검색)", description = "검색어와 매장 유형에 맞는 매장을 검색합니다.")
	@GetMapping("/search")
	public ResponseCustom<Page<SearchStoresResponse>> searchStores(
		Authentication authentication,
		@RequestParam(required = false, defaultValue = "") String keyword, Pageable pageable) {
		return ResponseCustom.OK(
			storeCommandService.searchStores(AuthenticationParser.parseUserId(authentication), keyword, pageable));
	}

	@Operation(summary = "가게 등록", description = "신규 파트너 가게를 등록합니다.")
	@PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseCustom<StoreCreateResponseDto> createStore(
		Authentication authentication,
		@Parameter(name = "image", description = "업로드 사진 데이터") @RequestPart(value = "image") MultipartFile image,
		@RequestPart(value = "store") StoreCreateRequest storeCreateRequest,
		@RequestPart(value = "menuImages", required = false) List<MultipartFile> menuImages) {

		StoreCreateResponseDto responseDto = storeCommandService.createStore(
			AuthenticationParser.parseUserId(authentication),
			storeCreateRequest, image,
			menuImages
		);

		return ResponseCustom.OK(responseDto);
	}

	@Operation(summary = "가게 정보 수정", description = "가게 정보를 수정합니다.")
	@PatchMapping("/update")
	public ResponseCustom<Message> updateStore(Authentication authentication,
		@RequestBody StoreUpdateRequest storeUpdateRequest) {
		storeCommandService.updateStore(AuthenticationParser.parseUserId(authentication), storeUpdateRequest);
		return ResponseCustom.OK(Message.builder().message("success").build());
	}

}