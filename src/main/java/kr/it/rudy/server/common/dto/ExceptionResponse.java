package kr.it.rudy.server.common.dto;

import lombok.Getter;

@Getter
public class ExceptionResponse {
    private final boolean success;
    private final String message;
    private final String errorMessage;

    private ExceptionResponse(boolean success, String message, String errorMessage) {
        this.success = success;
        this.message = message;
        this.errorMessage = errorMessage;
    }

    public static ExceptionResponse error(String errorMessage) {
        return new ExceptionResponse(false, "fail", errorMessage);
    }
}
