package com.resumeai.resumeAI.resume;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class PdfExtractorService {

    public String extractText(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("O arquivo enviado está vazio.");
        }

        if (!"application/pdf".equals(file.getContentType())) {
            throw new IllegalArgumentException("Apenas arquivos no formato PDF são suportados.");
        }

        try (PDDocument document = Loader.loadPDF(file.getBytes())) {
            PDFTextStripper pdfStripper = new PDFTextStripper();
            String text = pdfStripper.getText(document);

            if (text == null || text.trim().isEmpty()) {
                throw new IllegalArgumentException("Não foi possível extrair nenhum texto legível do PDF.");
            }

            return text.trim();

        } catch (IOException e) {
            throw new RuntimeException("Erro ao processar e ler o arquivo PDF: " + e.getMessage());
        }
    }
}
