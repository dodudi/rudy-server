package kr.it.rudy.server.jwt.domain.model;

public record JwtDecode(
        String header,
        String body
) {
}
