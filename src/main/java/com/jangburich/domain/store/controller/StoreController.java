package com.jangburich.domain.store.controller;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.jangburich.domain.store.domain.Category;
import com.jangburich.domain.store.domain.StoreCreateRequestDTO;
import com.jangburich.domain.store.domain.StoreGetResponseDTO;
import com.jangburich.domain.store.domain.StoreUpdateRequestDTO;
import com.jangburich.domain.store.dto.response.OrderDetailResponse;
import com.jangburich.domain.store.dto.response.OrderGetResponse;
import com.jangburich.domain.store.dto.response.OrderTodayResponse;
import com.jangburich.domain.store.dto.response.PaymentGroupDetailResponse;
import com.jangburich.domain.store.dto.response.SearchStoresResponse;
import com.jangburich.domain.store.dto.response.StoreSearchDetailsResponse;
import com.jangburich.domain.store.dto.response.StoreTeamResponse;
import com.jangburich.domain.store.service.StoreService;
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
public class StoreController {

	private final StoreService storeService;

	@Operation(summary = "카테고리 별 가게 목록 조회", description = "카테고리 별로 가게 목록을 조회합니다.")
	@PostMapping("/category")
	public ResponseCustom<Page<SearchStoresResponse>> searchByCategory(
		Authentication authentication,
		@RequestParam(required = false, defaultValue = "3") Integer searchRadius,
		@RequestParam(required = false, defaultValue = "전체") String category,
		Double lat, Double lon, Pageable pageable) {
		Category categoryEnum = Category.fromDisplayName(category);
		return ResponseCustom.OK(
			storeService.searchByCategory(AuthenticationParser.parseUserId(authentication), searchRadius, categoryEnum,
				lat, lon, pageable));
	}

	@Operation(summary = "매장 찾기(검색)", description = "검색어와 매장 유형에 맞는 매장을 검색합니다.")
	@GetMapping("/search")
	public ResponseCustom<Page<SearchStoresResponse>> searchStores(
		Authentication authentication,
		@RequestParam(required = false, defaultValue = "") String keyword, Pageable pageable) {
		return ResponseCustom.OK(
			storeService.searchStores(AuthenticationParser.parseUserId(authentication), keyword, pageable));
	}

	@Operation(summary = "매장 상세 페이지 조회", description = "매장을 상세 조회합니다.")
	@GetMapping("/{storeId}")
	public ResponseCustom<StoreSearchDetailsResponse> storeSearchDetails(
		Authentication authentication,
		@PathVariable Long storeId
	) {
		// TODO API 수정 필요
		return ResponseCustom.OK();
	}

	@Operation(summary = "가게 등록", description = "신규 파트너 가게를 등록합니다.")
	@PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseCustom<Message> createStore(
		Authentication authentication,
		@Parameter(name = "image", description = "업로드 사진 데이터") @RequestPart(value = "image") MultipartFile image,
		@RequestPart(value = "store") StoreCreateRequestDTO storeCreateRequestDTO,
		@RequestPart(value = "menuImages", required = false) List<MultipartFile> menuImages) {

		storeService.createStore(AuthenticationParser.parseUserId(authentication), storeCreateRequestDTO, image,
			menuImages);
		return ResponseCustom.OK(Message.builder().message("success").build());
	}

	@Operation(summary = "가게 정보 수정", description = "가게 정보를 수정합니다.")
	@PatchMapping("/update")
	public ResponseCustom<Message> updateStore(Authentication authentication,
		@RequestBody StoreUpdateRequestDTO storeUpdateRequestDTO) {
		storeService.updateStore(AuthenticationParser.parseUserId(authentication), storeUpdateRequestDTO);
		return ResponseCustom.OK(Message.builder().message("success").build());
	}

	@Operation(summary = "가게 정보 조회", description = "가게 상세 정보를 조회합니다.")
	@GetMapping("")
	public ResponseCustom<StoreGetResponseDTO> getStoreInfo(Authentication authentication) {
		return ResponseCustom.OK(storeService.getStoreInfo(AuthenticationParser.parseUserId(authentication)));
	}

	@Operation(summary = "결제 그룹 조회", description = "장부 결제 그룹을 조회합니다.")
	@GetMapping("/payment_group")
	public ResponseCustom<List<StoreTeamResponse>> getPaymentGroup(Authentication authentication) {
		return ResponseCustom.OK(
			storeService.getPaymentGroup(AuthenticationParser.parseUserId(authentication)));
	}

	@Operation(summary = "결제 그룹 상세 조회", description = "장부 결제 그룹을 상세 조회합니다.")
	@GetMapping("/payment_group/{teamId}")
	public ResponseCustom<PaymentGroupDetailResponse> getPaymentGroupDetail(Authentication authentication,
		@PathVariable Long teamId) {
		return ResponseCustom.OK(
			storeService.getPaymentGroupDetail(AuthenticationParser.parseUserId(authentication), teamId));
	}

	@Operation(summary = "결제 내역 조회", description = "가게에서 일어난 결제 내역을 조회합니다.")
	@GetMapping("/payment_history")
	public ResponseCustom<?> getPaymentHistory(Authentication authentication) {
		return ResponseCustom.OK(
			storeService.getPaymentHistory(AuthenticationParser.parseUserId(authentication)));
	}

	@Operation(summary = "지난 주문 조회", description = "가게에 있는 지난 주문을 조회합니다")
	@GetMapping("/orders/last")
	public ResponseCustom<List<OrderGetResponse>> getLastOrders(Authentication authentication) {
		List<OrderGetResponse> ordersLast = storeService.getOrdersLast(
			AuthenticationParser.parseUserId(authentication));
		return ResponseCustom.OK(ordersLast);
	}

	@Operation(summary = "오늘 주문 조회", description = "가게에 있는 오늘 주문을 조회합니다")
	@GetMapping("/orders/today")
	public ResponseCustom<OrderTodayResponse> getTodayOrders(Authentication authentication) {
		return ResponseCustom.OK(storeService.getTodayOrders(
			AuthenticationParser.parseUserId(authentication)));
	}

	@Operation(summary = "주문 상세 조회", description = "가게에 있는 주문을 상세 조회합니다")
	@GetMapping("/orders/{ordersId}")
	public ResponseCustom<OrderDetailResponse> getOrders(Authentication authentication, @RequestParam Long orderId) {
		return ResponseCustom.OK(
			storeService.getOrderDetails(AuthenticationParser.parseUserId(authentication), orderId));
	}

	@Operation(summary = "가게 엑셀 다운로드", description = "가게 장부 세부 내역을 엑셀로 제공합니다.")
	@GetMapping("/excel")
	public ResponseEntity<?> getExcel(
		Authentication authentication,
		@RequestParam(defaultValue = "1") Integer period
	) {
		byte[] excel = storeService.createExcel(AuthenticationParser.parseUserId(authentication), period);

		String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		String fileName = "장부_세부내역_" + period + "개월_" + today + ".xlsx";
		String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8)
			.replace("+", "%20");

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		headers.set("Content-Disposition",
			"attachment; filename=\"" + encodedFileName + "\"; filename*=UTF-8''" + encodedFileName);

		return ResponseEntity.ok().headers(headers).body(excel);
	}
}