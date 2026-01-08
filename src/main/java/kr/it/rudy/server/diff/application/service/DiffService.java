package kr.it.rudy.server.diff.application.service;

import kr.it.rudy.server.diff.application.dto.DiffRequest;
import kr.it.rudy.server.diff.application.dto.DiffResponse;
import kr.it.rudy.server.diff.domain.model.DiffLine;
import kr.it.rudy.server.diff.domain.model.DiffType;
import kr.it.rudy.server.diff.domain.service.DiffProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DiffService {

    private final DiffProcessor diffProcessor;

    public DiffResponse diff(DiffRequest request) {
        List<DiffLine> differences = diffProcessor.diff(
                request.originalText(),
                request.modifiedText()
        );

        int addedLines = (int) differences.stream()
                .filter(line -> line.type() == DiffType.INSERT)
                .count();

        int deletedLines = (int) differences.stream()
                .filter(line -> line.type() == DiffType.DELETE)
                .count();

        int unchangedLines = (int) differences.stream()
                .filter(line -> line.type() == DiffType.EQUAL)
                .count();

        return new DiffResponse(differences, addedLines, deletedLines, unchangedLines);
    }
}
