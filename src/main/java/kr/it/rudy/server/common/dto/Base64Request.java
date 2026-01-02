package kr.it.rudy.server.common.dto;

import jakarta.validation.constraints.NotEmpty;

public record Base64Request(
        @NotEmpty(message = "text must not be empty or nullo") String text
) {
}
