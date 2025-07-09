package com.jangburich.presentation.store.controller;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jangburich.application.store.service.StoreFileService;
import com.jangburich.utils.parser.AuthenticationParser;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Store", description = "Store API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/store")
public class StoreExcelController {
    private final StoreFileService storeFileService;

    @Operation(summary = "가게 엑셀 다운로드", description = "가게 장부 세부 내역을 엑셀로 제공합니다.")
    @GetMapping("/excel")
    public ResponseEntity<?> getExcel(
        Authentication authentication,
        @RequestParam(defaultValue = "1") Integer period
    ) {
        byte[] excel = storeFileService.createExcel(AuthenticationParser.parseUserId(authentication), period);

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
