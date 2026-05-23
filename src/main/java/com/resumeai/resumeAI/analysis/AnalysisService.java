package com.resumeai.resumeAI.analysis;

import com.resumeai.resumeAI.exceptions.ResourceNotFoundException;
import com.resumeai.resumeAI.resume.Resume;
import com.resumeai.resumeAI.resume.ResumeRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AnalysisService {

    private final AnalysisRepository analysisRepository;
    private final ResumeRepository resumeRepository;
    private final AiService aiService;

    public AnalysisService(AnalysisRepository analysisRepository,
                           ResumeRepository resumeRepository,
                           AiService aiService) {
        this.analysisRepository = analysisRepository;
        this.resumeRepository = resumeRepository;
        this.aiService = aiService;
    }

    public Analysis createAnalysis(Long resumeId) {
        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new ResourceNotFoundException("Currículo não encontrado com o ID: " + resumeId));

        String aiInsights = aiService.analyzeResumeText(resume.getExtractedText());

        Analysis analysis = new Analysis();
        analysis.setInsights(aiInsights);
        analysis.setAnalyzedAt(LocalDateTime.now());
        analysis.setResume(resume);

        return analysisRepository.save(analysis);
    }

}

