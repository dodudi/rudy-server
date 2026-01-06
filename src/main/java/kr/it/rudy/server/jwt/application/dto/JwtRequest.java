package kr.it.rudy.server.jwt.application.dto;

import jakarta.validation.constraints.NotEmpty;

public record JwtRequest(
        @NotEmpty(message = "token must not be empty or null") String token,
        String secretKey
) {
}
