package org.example.coderank.controller;

import jakarta.validation.Valid;
import org.example.coderank.model.Submission;
import org.example.coderank.service.SubmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/submissions")
public class SubmissionController {
    
    private final SubmissionService submissionService;
    
    @Autowired
    public SubmissionController(SubmissionService submissionService) {
        this.submissionService = submissionService;
    }
    
    @PostMapping
    public ResponseEntity<Submission> createSubmission(@Valid @RequestBody Submission submission) {
        Submission createdSubmission = submissionService.createSubmission(submission);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSubmission);
    }
    
    @GetMapping
    public ResponseEntity<List<Submission>> getAllSubmissions(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Long problemId,
            @RequestParam(required = false) String status) {
        
        List<Submission> submissions;
        
        if (userId != null) {
            submissions = submissionService.getSubmissionsByUserId(userId);
        } else if (problemId != null) {
            submissions = submissionService.getSubmissionsByProblemId(problemId);
        } else if (status != null) {
            submissions = submissionService.getSubmissionsByStatus(status);
        } else {
            submissions = submissionService.getAllSubmissions();
        }
        
        return ResponseEntity.ok(submissions);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Submission> getSubmissionById(@PathVariable Long id) {
        return submissionService.getSubmissionById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
