package com.jangburich.global.payload;

import lombok.Getter;

@Getter
public enum ErrorCode {

    INVALID_PARAMETER(400,null,"잘못된 요청 데이터입니다."),
    INVALID_REPRESENTATION(400,null,"잘못된 표현 입니다."),
    INVALID_FILE_PATH(400,null,"잘못된 파일 경로 입니다."),
    INVALID_OPTIONAL_ISPRESENT(400,null,"해당 값이 존재하지 않습니다."),
    INVALID_CHECK(400,null,"해당 값이 유효하지 않습니다."),
    INVALID_AUTHENTICATION(400,null,"잘못된 인증입니다."),
    INVALID_ORDER_ID(400,"INVALID_ORDER_ID","해당 금액권이 존재하지 않습니다."),
    INVALID_STORE_ID(400, null, "해당 store가 존재하지 않습니다."),
    INVALID_TEAM_ID(400,null,"해당 그룹이 존재하지 않습니다."),
    INVALID_USER_ID(400, null, "사용자 권한이 없습니다"),
    INVALID_STORE_TEAM_ID(400,"INVALID_STORE_TEAM_ID", "유효하지 않은 가게 id와 팀 id 입니다.");

    private final String code;
    private final String message;
    private final int status;

    ErrorCode(final int status, final String code, final String message) {
        this.status = status;
        this.message = message;
        this.code = code;
    }
}
