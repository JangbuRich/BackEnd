package com.jangburich.global.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ApiResponse {

    @Schema(type = "boolean", example = "true", description = "올바르게 로직을 처리했으면 True, 아니면 False를 반환합니다.")
    private boolean check;

    @Schema(type = "object", example = "information", description = "restful의 정보를 감싸 표현합니다. object형식으로 표현합니다.")
    private Object information;

    @Builder
    public ApiResponse(boolean check, Object information) {
        this.check = check;
        this.information = information;
    }
}
