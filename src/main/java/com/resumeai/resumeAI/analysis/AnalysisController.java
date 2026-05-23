package com.resumeai.resumeAI.analysis;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = ("/api/analyses"))
public class AnalysisController {

    private final AnalysisService analysisService;

    public AnalysisController(AnalysisService analysisService) {
        this.analysisService = analysisService;
    }

    @PostMapping("resume/{resumeId}")
    public ResponseEntity<AnalysisResponseDTO> triggerAnalysis(@PathVariable Long resumeId) {

        Analysis savedAnalysis = analysisService.createAnalysis(resumeId);

        AnalysisResponseDTO response = new AnalysisResponseDTO(
                savedAnalysis.getId(),
                savedAnalysis.getResume().getId(),
                savedAnalysis.getInsights()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }

}

record AnalysisResponseDTO(Long id, Long resumeId, String insights) {}