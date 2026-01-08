package kr.it.rudy.server.diff.domain.service;

import kr.it.rudy.server.diff.domain.model.DiffLine;
import kr.it.rudy.server.diff.domain.model.DiffType;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DiffProcessor {

    public List<DiffLine> diff(String originalText, String modifiedText) {
        String[] originalLines = splitLines(originalText);
        String[] modifiedLines = splitLines(modifiedText);

        return computeDiff(originalLines, modifiedLines);
    }

    private String[] splitLines(String text) {
        if (text == null || text.isEmpty()) {
            return new String[0];
        }
        return text.split("\n", -1);
    }

    private List<DiffLine> computeDiff(String[] original, String[] modified) {
        List<DiffLine> result = new ArrayList<>();
        int[][] lcs = computeLCS(original, modified);

        int i = original.length;
        int j = modified.length;
        int originalLineNum = i;
        int modifiedLineNum = j;

        while (i > 0 || j > 0) {
            if (i > 0 && j > 0 && original[i - 1].equals(modified[j - 1])) {
                result.add(0, new DiffLine(DiffType.EQUAL, original[i - 1], originalLineNum, modifiedLineNum));
                i--;
                j--;
                originalLineNum--;
                modifiedLineNum--;
            } else if (j > 0 && (i == 0 || lcs[i][j - 1] >= lcs[i - 1][j])) {
                result.add(0, new DiffLine(DiffType.INSERT, modified[j - 1], null, modifiedLineNum));
                j--;
                modifiedLineNum--;
            } else if (i > 0) {
                result.add(0, new DiffLine(DiffType.DELETE, original[i - 1], originalLineNum, null));
                i--;
                originalLineNum--;
            }
        }

        return result;
    }

    private int[][] computeLCS(String[] original, String[] modified) {
        int m = original.length;
        int n = modified.length;
        int[][] lcs = new int[m + 1][n + 1];

        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (original[i - 1].equals(modified[j - 1])) {
                    lcs[i][j] = lcs[i - 1][j - 1] + 1;
                } else {
                    lcs[i][j] = Math.max(lcs[i - 1][j], lcs[i][j - 1]);
                }
            }
        }

        return lcs;
    }
}
