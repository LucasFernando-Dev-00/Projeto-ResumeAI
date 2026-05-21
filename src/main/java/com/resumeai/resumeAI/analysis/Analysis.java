package com.resumeai.resumeAI.analysis;


import com.resumeai.resumeAI.resume.Resume;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "tb_analyses")
public class Analysis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String insights;

    private LocalDateTime analyzedAt = LocalDateTime.now();

    @OneToOne
    @JoinColumn(name = "resume_id", nullable = false)
    private Resume resume;

    public Analysis() {

    }

    public Analysis(Long id, String insights, LocalDateTime analyzedAt, Resume resume) {
        this.id = id;
        this.insights = insights;
        this.analyzedAt = analyzedAt;
        this.resume = resume;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInsights() {
        return insights;
    }

    public void setInsights(String insights) {
        this.insights = insights;
    }

    public LocalDateTime getAnalyzedAt() {
        return analyzedAt;
    }

    public void setAnalyzedAt(LocalDateTime analyzedAt) {
        this.analyzedAt = analyzedAt;
    }

    public Resume getResume() {
        return resume;
    }

    public void setResume(Resume resume) {
        this.resume = resume;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Analysis analysis = (Analysis) o;
        return Objects.equals(id, analysis.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
