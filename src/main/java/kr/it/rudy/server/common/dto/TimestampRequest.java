package kr.it.rudy.server.common.dto;

public record TimestampRequest(
        String input,
        String conversionType,
        String timezone
) {
}
