package com.docfast.analysis;

public record AnalysisResponse(
        String result,
        int extractedChars,
        double estimatedCostBrl) {
}
