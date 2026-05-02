package com.docfast.analysis;

import com.docfast.common.exception.ValidationException;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.tika.Tika;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Set;

@Service
public class TextExtractor {

    private static final Logger log = LoggerFactory.getLogger(TextExtractor.class);

    private static final Set<String> ALLOWED_MIMES = Set.of(
            "application/pdf",
            "image/jpeg",
            "image/png",
            "image/webp");

    private final Tika tika = new Tika();

    /**
     * Extrai texto de PDF ou imagem. Valida MIME real do arquivo.
     */
    public String extract(MultipartFile file) {
        validateFile(file);

        String mimeType = detectMimeType(file);
        log.info("Arquivo detectado: {} ({})", file.getOriginalFilename(), mimeType);

        return switch (mimeType) {
            case "application/pdf" -> extractFromPdf(file);
            case "image/jpeg", "image/png", "image/webp" -> extractFromImage(file);
            default -> throw new ValidationException(
                    "Tipo de arquivo não suportado. Envie um PDF ou imagem (JPG, PNG, WEBP).");
        };
    }

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new ValidationException("Nenhum arquivo foi enviado.");
        }

        if (file.getSize() > 10 * 1024 * 1024) {
            throw new ValidationException("Arquivo muito grande. Envie um arquivo de até 10 MB.");
        }
    }

    private String detectMimeType(MultipartFile file) {
        try {
            String detected = tika.detect(file.getBytes());
            if (!ALLOWED_MIMES.contains(detected)) {
                throw new ValidationException(
                        "Tipo de arquivo não permitido. Envie apenas PDF, JPG, PNG ou WEBP.");
            }
            return detected;
        } catch (IOException e) {
            throw new ValidationException("Não foi possível ler o arquivo.");
        }
    }

    private String extractFromPdf(MultipartFile file) {
        try (PDDocument doc = Loader.loadPDF(file.getBytes())) {
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(doc);

            if (text.isBlank() || text.length() < 100) {
                throw new ValidationException(
                        "PDF sem texto legível. Se for PDF escaneado, envie como imagem (JPG/PNG) para leitura automática.");
            }

            log.info("Texto extraído do PDF: {} caracteres", text.length());
            return text.trim();

        } catch (IOException e) {
            log.error("Erro ao processar PDF: {}", e.getMessage(), e);
            throw new ValidationException("Erro ao ler o PDF. Verifique se o arquivo não está corrompido.");
        }
    }

    private String extractFromImage(MultipartFile file) {
        // TODO: Implementar OCR com Tesseract.js no client ou Google Vision aqui
        // Por ora, retorna placeholder
        log.warn("OCR não implementado ainda. Arquivo: {}", file.getOriginalFilename());
        throw new ValidationException(
                "Leitura de imagem em desenvolvimento. Por enquanto, use apenas PDFs com texto.");
    }
}
