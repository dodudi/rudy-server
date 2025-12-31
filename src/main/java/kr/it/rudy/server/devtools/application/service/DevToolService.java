package kr.it.rudy.server.devtools.application.service;

import kr.it.rudy.server.common.dto.JsonParserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

@Service
@RequiredArgsConstructor
public class DevToolService {
    private final ObjectMapper objectMapper;

    public JsonParserResponse jsonParser(String json) {
        try {
            // JSON 유효성 검사 및 파싱
            Object parsedJson = objectMapper.readValue(json, Object.class);

            // 포맷팅된 JSON 생성
            String formattedJson = objectMapper.writerWithDefaultPrettyPrinter()
                    .writeValueAsString(parsedJson);

            // 최소화된 JSON 생성
            String minifiedJson = objectMapper.writeValueAsString(parsedJson);

            return new JsonParserResponse(
                    formattedJson,
                    minifiedJson,
                    true,
                    null
            );
        } catch (Exception e) {
            return new JsonParserResponse(
                    null,
                    null,
                    false,
                    e.getMessage()
            );
        }
    }
}
