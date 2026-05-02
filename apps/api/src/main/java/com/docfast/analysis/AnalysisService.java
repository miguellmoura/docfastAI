package com.docfast.analysis;

import com.docfast.ai.AiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class AnalysisService {

    private static final Logger log = LoggerFactory.getLogger(AnalysisService.class);

    private final TextExtractor textExtractor;
    private final AiService aiService;

    public AnalysisService(TextExtractor textExtractor, AiService aiService) {
        this.textExtractor = textExtractor;
        this.aiService = aiService;
    }

    /**
     * Orquestra: extração de texto → resumo com IA.
     */
    public AnalysisResponse analyze(MultipartFile file, AnalysisType type) {
        log.info("Iniciando análise: {} - {}", file.getOriginalFilename(), type);

        // 1. Extrai texto
        String extractedText = textExtractor.extract(file);

        // 2. Gera resumo com IA
        String result = switch (type) {
            case SUMMARIZE -> aiService.summarize(extractedText);
            case EXTRACT_KEY_POINTS -> aiService.summarize(extractedText); // Por ora, mesmo fluxo
        };

        return new AnalysisResponse(
                result,
                extractedText.length(),
                estimateCost(extractedText.length()));
    }

    private double estimateCost(int textLength) {
        // Estimativa rough: ~1500 tokens (input + output) com gpt-4o-mini = ~$0.0003
        // Conversão: $1 ≈ R$5.50 → ~R$0.0017 por análise
        return 0.0017;
    }
}
