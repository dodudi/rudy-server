package kr.it.rudy.server.jwt.application.dto;

import jakarta.validation.constraints.NotBlank;

public record JwtDecodeRequest(
        @NotBlank(message = "JWT token must not be empty")
        String token
) {
}
