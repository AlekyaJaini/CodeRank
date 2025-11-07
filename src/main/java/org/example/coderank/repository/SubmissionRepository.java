package org.example.coderank.repository;

import org.example.coderank.model.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, Long> {
    
    List<Submission> findByUserId(Long userId);
    
    List<Submission> findByProblemId(UUID problemId);
    
    List<Submission> findByStatus(String status);
}
