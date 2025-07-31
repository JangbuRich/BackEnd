package com.jangburich.presentation.store.controller.command;

import java.util.List;

import com.jangburich.presentation.store.dtos.response.store.StoreListItem;
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

    @Operation(summary = "가게 정보 수정", description = "가게 정보를 수정합니다.")
    @PatchMapping("/update")
    public ResponseCustom<Message> updateStore(Authentication authentication, @RequestBody StoreUpdateRequest storeUpdateRequest) {
        storeCommandService.updateStore(AuthenticationParser.parseUserId(authentication), storeUpdateRequest);
        return ResponseCustom.OK(Message.builder().message("success").build());
    }

}