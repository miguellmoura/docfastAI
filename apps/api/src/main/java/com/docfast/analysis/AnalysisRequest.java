package com.docfast.analysis;

import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record AnalysisRequest(
        @NotNull(message = "Envie um arquivo") MultipartFile file,

        AnalysisType type) {
    public AnalysisRequest {
        if (type == null) {
            type = AnalysisType.SUMMARIZE; // Default
        }
    }
}
