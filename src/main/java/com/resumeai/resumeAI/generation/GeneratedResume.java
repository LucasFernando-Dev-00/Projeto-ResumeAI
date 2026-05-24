package com.resumeai.resumeAI.generation;

import com.resumeai.resumeAI.user.User;
import jakarta.persistence.*;


import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "tb_generated_resumes")
public class GeneratedResume {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String optimizedText;

    private String pdfUrl;

    private LocalDateTime generatedAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public GeneratedResume() {

    }

    public GeneratedResume(Long id, String optimizedText, String pdfUrl, LocalDateTime generatedAt, User user) {
        this.id = id;
        this.optimizedText = optimizedText;
        this.pdfUrl = pdfUrl;
        this.generatedAt = generatedAt;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOptimizedText() {
        return optimizedText;
    }

    public void setOptimizedText(String optimizedText) {
        this.optimizedText = optimizedText;
    }

    public String getPdfUrl() {
        return pdfUrl;
    }

    public void setPdfUrl(String pdfUrl) {
        this.pdfUrl = pdfUrl;
    }

    public LocalDateTime getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(LocalDateTime generatedAt) {
        this.generatedAt = generatedAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        GeneratedResume that = (GeneratedResume) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
