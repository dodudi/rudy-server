package kr.it.rudy.server.diff.application.dto;

import jakarta.validation.constraints.NotNull;

public record DiffRequest(
        @NotNull(message = "originalText must not be null") String originalText,
        @NotNull(message = "modifiedText must not be null") String modifiedText
) {
}
