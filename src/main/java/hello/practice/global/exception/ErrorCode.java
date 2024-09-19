package hello.practice.global.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    // 공통
    INVALID_INPUT_VALUE(400, "COMMON-001", "유효성 검증에 실패한 경우"),

    // 계정
    ALREADY_EXISTS_USERNAME(400, "ACCOUNT-001", "이미 존재하는 아이디일 경우"),
    ALREADY_EXISTS_EMAIL(400, "ACCOUNT-002", "이미 존재하는 이메일일 경우"),
    ALREADY_EXISTS_NICKNAME(400, "ACCOUNT-003", "이미 존재하는 닉네임일 경우"),
    UNAUTHORIZED(401, "ACCOUNT-004", "인증에 실패한 경우"),
    ACCOUNT_NOT_FOUND(404, "ACCOUNT-005", "계정을 찾을 수 없는 경우"),

    // 게시물
    BOARD_NOT_FOUND(404, "BOARD-001", "게시물을 찾을 수 없는 경우"),

    // Jwt
    ACCESS_TOKEN_NOT_FOUND(403, "JWT-001", "액세스 토큰이 존재하지 않는 경우"),
    ACCESS_TOKEN_EXPIRED(400, "JWT-002", "액세스 토큰이 만료되었을 경우"),
    INVALID_ACCESS_TOKEN(401, "JWT-003", "유효하지 않은 액세스 토큰일 경우"),
    REFRESH_TOKEN_NOT_FOUND(403, "JWT-004", "리프레시 토큰이 존재하지 않는 경우"),
    REFRESH_TOKEN_EXPIRED(400, "JWT-005", "리프레시 토큰이 만료되었을 경우"),
    INVALID_REFRESH_TOKEN(401, "JWT-006", "유효하지 않은 리프레시 토큰일 경우");

    private final int status;
    private final String code;
    private final String message;

    ErrorCode(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }


}
