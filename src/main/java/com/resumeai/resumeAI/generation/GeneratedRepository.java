package com.resumeai.resumeAI.generation;

import com.resumeai.resumeAI.analysis.Analysis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GeneratedRepository extends JpaRepository<GeneratedResume, Long> {

    List<GeneratedResume> findByUserId(Long userId);

}
