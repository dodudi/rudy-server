package kr.it.rudy.server.common.dto;

public record TimestampResponse(
        Long unixTimestampMillis,
        Long unixTimestampSeconds,
        String iso8601,
        String formatted,
        String rfc2822,
        String timezone,
        boolean success,
        String errorMessage
) {
}
