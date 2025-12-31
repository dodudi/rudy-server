package kr.it.rudy.server.common.dto;

public record JsonParserResponse(
        String formattedJson,
        String minifiedJson,
        boolean isValid,
        String errorMessage
) {
}
