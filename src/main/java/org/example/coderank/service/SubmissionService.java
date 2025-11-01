package org.example.coderank.service;

import org.example.coderank.model.Submission;
import org.example.coderank.repository.SubmissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SubmissionService {
    
    private final SubmissionRepository submissionRepository;
    
    @Autowired
    public SubmissionService(SubmissionRepository submissionRepository) {
        this.submissionRepository = submissionRepository;
    }
    
    public Submission createSubmission(Submission submission) {
        // Here you would typically add code execution logic
        // For now, we'll just save the submission with a default status
        if (submission.getStatus() == null) {
            submission.setStatus("PENDING");
        }
        return submissionRepository.save(submission);
    }
    
    public List<Submission> getAllSubmissions() {
        return submissionRepository.findAll();
    }
    
    public Optional<Submission> getSubmissionById(Long id) {
        return submissionRepository.findById(id);
    }
    
    public List<Submission> getSubmissionsByUserId(Long userId) {
        return submissionRepository.findByUserId(userId);
    }
    
    public List<Submission> getSubmissionsByProblemId(Long problemId) {
        return submissionRepository.findByProblemId(problemId);
    }
    
    public List<Submission> getSubmissionsByStatus(String status) {
        return submissionRepository.findByStatus(status);
    }
}
