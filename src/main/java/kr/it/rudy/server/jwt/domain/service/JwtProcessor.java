package kr.it.rudy.server.jwt.domain.service;

import org.springframework.stereotype.Component;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.util.Base64;
import java.util.Map;

@Component
public class JwtProcessor {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public Map<String, Object> decodeJwt(String jwt) {
        String decode = new String(Base64.getUrlDecoder().decode(jwt));
        return objectMapper.readValue(decode, new TypeReference<>() {
        });
    }
}
