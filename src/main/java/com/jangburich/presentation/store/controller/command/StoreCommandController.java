package com.jangburich.presentation.store.controller.command;

import java.time.LocalDateTime;
import java.util.List;

import com.jangburich.global.payload.BaseResponse;
import com.jangburich.presentation.store.dtos.response.store.StoreInfoResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.jangburich.global.payload.BaseResponse;
import com.jangburich.global.payload.CommonApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.jangburich.presentation.store.dtos.request.StoreCreateRequest;
import com.jangburich.presentation.store.dtos.response.store.StoreCreateResponseDto;
import com.jangburich.presentation.store.dtos.request.StoreUpdateRequest;
import com.jangburich.application.store.service.command.StoreCommandService;
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

    @Operation(summary = "가게 등록", description = "신규 파트너 가게를 등록합니다.")
    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseCustom<StoreCreateResponseDto> createStore(Authentication authentication, @Parameter(name = "image", description = "업로드 사진 데이터") @RequestPart(value = "image") MultipartFile image, @RequestPart(value = "store") StoreCreateRequest storeCreateRequest, @RequestPart(value = "menuImages", required = false) List<MultipartFile> menuImages) {

        StoreCreateResponseDto responseDto = storeCommandService.createStore(AuthenticationParser.parseUserId(authentication), storeCreateRequest, image, menuImages);

        return ResponseCustom.OK(responseDto);
    }

    @Operation(summary = "매장 정보 수정", description = "[매장 정보 관리 Tab] 매장 정보를 수정합니다.")
    @PatchMapping("/update")
    public ResponseEntity<?> updateStore(Authentication authentication, @Valid @RequestBody StoreUpdateRequest storeUpdateRequest) {
        storeCommandService.updateStore(AuthenticationParser.parseUserId(authentication), storeUpdateRequest);

        return ResponseEntity.ok(new BaseResponse<>(null, LocalDateTime.now(), "OK"));
    }

    @PostMapping("/{storeId}/like")
    @Operation(summary = "가게 단골 등록", description = "가게 좋아요를 등록합니다.", responses = {@ApiResponse(responseCode = "204", description = "No Content", content = @Content(schema = @Schema(implementation = BaseResponse.class)))})
    @CommonApiResponse
    public ResponseEntity<Void> createFavoriteStore(Authentication authentication, @PathVariable Long storeId) {
        storeCommandService.createFavoriteStore(AuthenticationParser.parseUserId(authentication), storeId);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{storeId}/like")
    @Operation(summary = "가게 단골 등록을 삭제", description = "가게 단골 등록을 삭제합니다", responses = {@ApiResponse(responseCode = "204", description = "No Content", content = @Content(schema = @Schema(implementation = BaseResponse.class)))})
    @CommonApiResponse
    public ResponseEntity<Void> deleteFavoriteStore(Authentication authentication, @PathVariable Long storeId) {
        storeCommandService.deleteFavoriteStore(AuthenticationParser.parseUserId(authentication), storeId);

        return ResponseEntity.noContent().build();
    }
}