package hello.practice.global.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    // 공통
    INVALID_INPUT_VALUE(400, "COMMON-001", "유효성 검증에 실패한 경우"),

    // 계정
    ALREADY_EXISTS_USERNAME(400, "ACCOUNT-001", "이미 존재하는 아이디일 경우"),
    ALREADY_EXISTS_EMAIL(400, "ACCOUNT-002", "이미 존재하는 이메일일 경우"),
    ALREADY_EXISTS_NICKNAME(400, "ACCOUNT-003", "이미 존재하는 닉네임일 경우");

    private final int status;
    private final String code;
    private final String message;

    ErrorCode(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }


}
