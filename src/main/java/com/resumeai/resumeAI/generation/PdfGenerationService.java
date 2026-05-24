package com.resumeai.resumeAI.generation;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;
import org.springframework.stereotype.Service;


import java.io.ByteArrayOutputStream;

@Service
public class PdfGenerationService {

    public byte[] generateResumePdf(String content) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter writer = new PdfWriter(out);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            Paragraph title = new Paragraph("CURRÍCULO OTIMIZADO - RESUMEAI")
                    .setFontSize(18)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(20);
            document.add(title);

        String[] lines = content.split("\n");
        for (String line : lines) {
            if (!line.trim().isEmpty()) {
                document.add(new Paragraph(line.trim()).setFontSize(11).setMarginBottom(5));
            }
        }

        document.close();

        return out.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Erro catastrófico ao gerar o arquivo PDF: " + e.getMessage());
        }
    }
}
