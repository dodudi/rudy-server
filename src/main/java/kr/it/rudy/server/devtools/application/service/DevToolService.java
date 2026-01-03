package kr.it.rudy.server.devtools.application.service;

import kr.it.rudy.server.common.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tools.jackson.core.util.DefaultIndenter;
import tools.jackson.core.util.DefaultPrettyPrinter;
import tools.jackson.databind.ObjectMapper;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

@Service
@RequiredArgsConstructor
public class DevToolService {
    private final ObjectMapper objectMapper;

    public JsonParserResponse jsonParser(String json, int indentSize) {
        try {
            // JSON 유효성 검사 및 파싱
            Object parsedJson = objectMapper.readValue(json, Object.class);

            // 포맷팅된 JSON 생성
            String indent = " ".repeat(Math.max(0, indentSize));

            DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
            printer.indentObjectsWith(new DefaultIndenter(indent, "\n"));
            printer.indentArraysWith(new DefaultIndenter(indent, "\n"));

            String formattedJson = objectMapper.writer()
                    .with(printer)
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

    public Base64Response encodeBase64(String text) {
        try {
            if (text == null || text.isEmpty()) {
                return new Base64Response(null, false, "입력 텍스트가 비어있습니다.");
            }
            String encoded = Base64.getEncoder().encodeToString(text.getBytes());
            return new Base64Response(encoded, true, null);
        } catch (Exception e) {
            return new Base64Response(null, false, e.getMessage());
        }
    }

    public Base64Response decodeBase64(String text) {
        try {
            if (text == null || text.isEmpty()) {
                return new Base64Response(null, false, "입력 텍스트가 비어있습니다.");
            }
            byte[] decodedBytes = Base64.getDecoder().decode(text);
            String decoded = new String(decodedBytes);
            return new Base64Response(decoded, true, null);
        } catch (IllegalArgumentException e) {
            return new Base64Response(null, false, "유효하지 않은 Base64 문자열입니다.");
        } catch (Exception e) {
            return new Base64Response(null, false, e.getMessage());
        }
    }

    public UrlResponse encodeUrl(String text) {
        try {
            if (text == null || text.isEmpty()) {
                return new UrlResponse(null, false, "입력 텍스트가 비어있습니다.");
            }
            String encoded = URLEncoder.encode(text, StandardCharsets.UTF_8);
            return new UrlResponse(encoded, true, null);
        } catch (Exception e) {
            return new UrlResponse(null, false, e.getMessage());
        }
    }

    public UrlResponse decodeUrl(String text) {
        try {
            if (text == null || text.isEmpty()) {
                return new UrlResponse(null, false, "입력 텍스트가 비어있습니다.");
            }
            String decoded = URLDecoder.decode(text, StandardCharsets.UTF_8);
            return new UrlResponse(decoded, true, null);
        } catch (IllegalArgumentException e) {
            return new UrlResponse(null, false, "유효하지 않은 URL 인코딩 문자열입니다.");
        } catch (Exception e) {
            return new UrlResponse(null, false, e.getMessage());
        }
    }

    public RegexResponse testRegex(String patternStr, String text, Boolean caseInsensitive, Boolean multiline, Boolean dotAll) {
        try {
            if (patternStr == null || patternStr.isEmpty()) {
                return new RegexResponse(false, List.of(), 0, false, "정규표현식 패턴이 비어있습니다.");
            }
            if (text == null || text.isEmpty()) {
                return new RegexResponse(false, List.of(), 0, false, "테스트 텍스트가 비어있습니다.");
            }

            // 플래그 설정
            int flags = 0;
            if (Boolean.TRUE.equals(caseInsensitive)) {
                flags |= Pattern.CASE_INSENSITIVE;
            }
            if (Boolean.TRUE.equals(multiline)) {
                flags |= Pattern.MULTILINE;
            }
            if (Boolean.TRUE.equals(dotAll)) {
                flags |= Pattern.DOTALL;
            }

            // 패턴 컴파일
            Pattern pattern = Pattern.compile(patternStr, flags);
            Matcher matcher = pattern.matcher(text);

            // 모든 매칭 찾기
            List<RegexMatch> matches = new ArrayList<>();
            while (matcher.find()) {
                List<String> groups = new ArrayList<>();
                // 그룹 추출 (0번 그룹은 전체 매칭이므로 1번부터)
                for (int i = 1; i <= matcher.groupCount(); i++) {
                    groups.add(matcher.group(i));
                }

                matches.add(new RegexMatch(
                        matcher.group(),
                        matcher.start(),
                        matcher.end(),
                        groups
                ));
            }

            return new RegexResponse(
                    true,
                    matches,
                    matches.size(),
                    true,
                    null
            );
        } catch (PatternSyntaxException e) {
            return new RegexResponse(
                    false,
                    List.of(),
                    0,
                    false,
                    "잘못된 정규표현식 패턴: " + e.getMessage()
            );
        } catch (Exception e) {
            return new RegexResponse(
                    false,
                    List.of(),
                    0,
                    false,
                    e.getMessage()
            );
        }
    }

    public TimestampResponse convertTimestamp(String input, String conversionType, String timezone) {
        try {
            ZoneId zoneId;
            try {
                zoneId = (timezone == null || timezone.isEmpty()) ? ZoneId.systemDefault() : ZoneId.of(timezone);
            } catch (Exception e) {
                return new TimestampResponse(null, null, null, null, null, null, false, "유효하지 않은 타임존입니다: " + timezone);
            }

            ZonedDateTime dateTime;

            if ("CURRENT_TIME".equals(conversionType)) {
                dateTime = ZonedDateTime.now(zoneId);
            } else if ("TO_DATE".equals(conversionType)) {
                if (input == null || input.isEmpty()) {
                    return new TimestampResponse(null, null, null, null, null, null, false, "입력값이 비어있습니다.");
                }
                try {
                    long timestamp = Long.parseLong(input.trim());
                    if (timestamp < 10000000000L) {
                        dateTime = Instant.ofEpochSecond(timestamp).atZone(zoneId);
                    } else {
                        dateTime = Instant.ofEpochMilli(timestamp).atZone(zoneId);
                    }
                } catch (NumberFormatException e) {
                    return new TimestampResponse(null, null, null, null, null, null, false, "유효하지 않은 타임스탬프 숫자입니다.");
                } catch (Exception e) {
                    return new TimestampResponse(null, null, null, null, null, null, false, "타임스탬프 변환 실패: " + e.getMessage());
                }
            } else if ("TO_TIMESTAMP".equals(conversionType)) {
                if (input == null || input.isEmpty()) {
                    return new TimestampResponse(null, null, null, null, null, null, false, "입력값이 비어있습니다.");
                }
                try {
                    dateTime = parseDateTime(input.trim(), zoneId);
                } catch (DateTimeParseException e) {
                    return new TimestampResponse(null, null, null, null, null, null, false, "날짜 형식이 잘못되었습니다. ISO 8601 형식(예: 2026-01-03T15:30:00)을 사용하세요.");
                } catch (Exception e) {
                    return new TimestampResponse(null, null, null, null, null, null, false, "날짜 파싱 실패: " + e.getMessage());
                }
            } else {
                return new TimestampResponse(null, null, null, null, null, null, false, "유효하지 않은 변환 타입입니다.");
            }

            long millis = dateTime.toInstant().toEpochMilli();
            long seconds = dateTime.toInstant().getEpochSecond();
            String iso8601 = dateTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
            String formatted = formatKorean(dateTime);
            String rfc2822 = dateTime.format(DateTimeFormatter.RFC_1123_DATE_TIME);
            String timezoneStr = dateTime.getZone().getId();

            return new TimestampResponse(millis, seconds, iso8601, formatted, rfc2822, timezoneStr, true, null);

        } catch (Exception e) {
            return new TimestampResponse(null, null, null, null, null, null, false, "변환 중 오류 발생: " + e.getMessage());
        }
    }

    private ZonedDateTime parseDateTime(String input, ZoneId zoneId) {
        DateTimeFormatter[] formatters = {
                DateTimeFormatter.ISO_DATE_TIME,
                DateTimeFormatter.ISO_LOCAL_DATE_TIME,
                DateTimeFormatter.ISO_OFFSET_DATE_TIME,
                DateTimeFormatter.ISO_ZONED_DATE_TIME,
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"),
                DateTimeFormatter.ofPattern("yyyy-MM-dd")
        };

        for (DateTimeFormatter formatter : formatters) {
            try {
                if (input.contains("T") || input.contains("Z") || input.contains("+")) {
                    return ZonedDateTime.parse(input, formatter);
                } else {
                    LocalDateTime localDateTime = LocalDateTime.parse(input, formatter);
                    return localDateTime.atZone(zoneId);
                }
            } catch (DateTimeParseException ignored) {
            }
        }

        throw new DateTimeParseException("Unable to parse date", input, 0);
    }

    private String formatKorean(ZonedDateTime dateTime) {
        int year = dateTime.getYear();
        int month = dateTime.getMonthValue();
        int day = dateTime.getDayOfMonth();
        int hour = dateTime.getHour();
        int minute = dateTime.getMinute();
        int second = dateTime.getSecond();

        String amPm = hour < 12 ? "오전" : "오후";
        int hour12 = hour % 12;
        if (hour12 == 0) hour12 = 12;

        return String.format("%d년 %d월 %d일 %s %d시 %02d분 %02d초",
                year, month, day, amPm, hour12, minute, second);
    }
}
