package com.resumeai.resumeAI.generation;


import com.resumeai.resumeAI.analysis.Analysis;
import com.resumeai.resumeAI.analysis.AnalysisRepository;
import com.resumeai.resumeAI.exceptions.ResourceNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/generation")
public class GenerationController {

    private final PdfGenerationService pdfGenerationService;

    private final AnalysisRepository analysisRepository;

    public GenerationController(PdfGenerationService pdfGenerationService, AnalysisRepository analysisRepository) {
        this.pdfGenerationService = pdfGenerationService;
        this.analysisRepository = analysisRepository;
    }

    @GetMapping("/download/{analysisId}")
    public ResponseEntity<byte[]> downloadOptimizedResume(@PathVariable Long analysisId) {

    Analysis analysis = analysisRepository.findById(analysisId)
            .orElseThrow(() -> new ResourceNotFoundException("Análise não encontrada com o ID: " + analysisId));


    byte[] pdfBytes = pdfGenerationService.generateResumePdf(analysis.getInsights());

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_PDF);
    headers.setContentDispositionFormData("attachment", "Curriculo_Otimizado.pdf");
    headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

    return ResponseEntity.ok()
            .headers(headers)
            .body(pdfBytes);
    }
}
