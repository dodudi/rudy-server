package kr.it.rudy.server.diff.application.dto;

import kr.it.rudy.server.diff.domain.model.DiffLine;

import java.util.List;

public record DiffResponse(
        List<DiffLine> differences,
        int addedLines,
        int deletedLines,
        int unchangedLines
) {
}
