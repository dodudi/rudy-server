package kr.it.rudy.server.diff.domain.model;

public record DiffLine(
        DiffType type,
        String content,
        Integer originalLineNumber,
        Integer modifiedLineNumber
) {
}
