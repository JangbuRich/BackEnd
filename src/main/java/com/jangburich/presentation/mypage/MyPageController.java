package com.jangburich.presentation.mypage;

import com.jangburich.application.mypage.MyPageService;
import com.jangburich.global.payload.BaseResponse;
import com.jangburich.global.payload.MyPageDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/mypage")
@RequiredArgsConstructor
@Tag(name = "MyPage", description = "마이페이지 API")
public class MyPageController {

    private final MyPageService myPageService;

    @GetMapping("/{userId}")
    @Operation(summary = "마이페이지 정보 조회", description = "사용자의 마이페이지 정보를 조회합니다.")
    public ResponseEntity<?> getMyPageInfo(@PathVariable Long userId) {
        
        MyPageDto.MyPageResponse response = myPageService.getMyPageInfo(userId);

        return ResponseEntity.ok(new BaseResponse<>(response, LocalDateTime.now(), "OK"));
    }
}
