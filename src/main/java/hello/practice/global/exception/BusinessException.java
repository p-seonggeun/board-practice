package hello.practice.global.exception;

import lombok.Getter;

public class BusinessException extends RuntimeException {

    @Getter
    private ErrorCode errorCode;
    private String message;

    public BusinessException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
