package kr.it.rudy.server.common.dto;

public record Base64Response(
        String result,
        boolean success,
        String errorMessage
) {
}
