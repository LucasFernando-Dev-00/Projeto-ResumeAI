package com.resumeai.resumeAI.resume;

import com.resumeai.resumeAI.user.User;
import com.resumeai.resumeAI.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Service
public class ResumeService {

    private final ResumeRepository resumeRepository;
    private final UserRepository userRepository;
    private final PdfExtractorService pdfExtractorService;

    public ResumeService(ResumeRepository resumeRepository,
                         UserRepository userRepository,
                         PdfExtractorService pdfExtractorService) {
        this.resumeRepository = resumeRepository;
        this.userRepository = userRepository;
        this.pdfExtractorService = pdfExtractorService;
    }

    public Resume uploadAndExtract(MultipartFile file, String userEmail) {

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com o e-mail: " + userEmail));

        String extractedText = pdfExtractorService.extractText(file);

        Resume resume = new Resume();
        resume.setFileName(file.getOriginalFilename());
        resume.setExtractedText(extractedText);
        resume.setUploadedAt(LocalDateTime.now());
        resume.setUser(user);

        return resumeRepository.save(resume);
    }


}
