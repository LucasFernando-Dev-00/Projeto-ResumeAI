package com.resumeai.resumeAI.resume;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "/api/resumes")
public class ResumeController {

    private final ResumeService resumeService;

    public ResumeController(ResumeService resumeService) {
       this.resumeService = resumeService;
    }

    @PostMapping("/upload")
    public ResponseEntity<ResumeResponseDTO> uploadResum(
            @RequestParam("file") MultipartFile file,
            Authentication authentication) {

        String userEmail = authentication.getName();

        Resume savedResume = resumeService.uploadAndExtract(file, userEmail);

        ResumeResponseDTO responseDTO = new ResumeResponseDTO(
                savedResume.getId(),
                savedResume.getFileName(),
                "Currículo enviado e texto extraído com sucesso!"
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);

    }
}

record ResumeResponseDTO(Long id, String fileName, String message) {}