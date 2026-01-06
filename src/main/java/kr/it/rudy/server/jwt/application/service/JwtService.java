package kr.it.rudy.server.jwt.application.service;

import kr.it.rudy.server.jwt.application.dto.JwtDecodeResponse;
import kr.it.rudy.server.jwt.domain.service.JwtProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtProcessor jwtProcessor;

    public JwtDecodeResponse decodeJwt(String token) {
        // JWT 토큰을 3개 부분으로 분리 (Header.Payload.Signature)
        String[] parts = token.split("\\.");

        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid JWT token");
        }

        // JWT 파싱
        Map<String, Object> header = jwtProcessor.decodeJwt(parts[0]);
        Map<String, Object> payload = jwtProcessor.decodeJwt(parts[1]);

        // Signature (Base64 인코딩된 상태로 반환)
        String signature = parts[2];

        // Header에서 알고리즘과 타입 추출
        String algorithm = (String) header.getOrDefault("alg", "Unknown");
        String type = (String) header.getOrDefault("typ", "JWT");

        // Payload에서 시간 정보 추출
        Long issuedAt = extractTimestamp(payload, "iat");
        Long expiresAt = extractTimestamp(payload, "exp");

        // 만료 여부 확인
        boolean isExpired = false;
        if (expiresAt != null) {
            isExpired = System.currentTimeMillis() / 1000 > expiresAt;
        }

        return new JwtDecodeResponse(header, payload, signature, true, algorithm, type, issuedAt, expiresAt, isExpired);
    }


    private Long extractTimestamp(Map<String, Object> payload, String key) {
        Object value = payload.get(key);
        if (value instanceof Number) {
            return ((Number) value).longValue();
        }
        return null;
    }
}
