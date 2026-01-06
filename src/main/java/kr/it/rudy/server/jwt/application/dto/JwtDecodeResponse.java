package kr.it.rudy.server.jwt.application.dto;

import java.util.Map;

public record JwtDecodeResponse(
        Map<String, Object> header,
        Map<String, Object> payload,
        String signature,
        boolean isValid,
        String algorithm,
        String type,
        Long issuedAt,
        Long expiresAt,
        boolean isExpired
) {
}
