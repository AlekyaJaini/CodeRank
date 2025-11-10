package org.example.coderank.controller;

import jakarta.validation.Valid;
import org.example.coderank.dto.SubmissionResponseDTO;
import org.example.coderank.model.Submission;
import org.example.coderank.service.SubmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.example.coderank.dto.SubmsissionRequestDTO;

import java.util.*;

@RestController
@RequestMapping("/submissions")
public class SubmissionController {

    private final SubmissionService submissionService;

    @Autowired
    public SubmissionController(SubmissionService submissionService) {
        this.submissionService = submissionService;
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createSubmission(@Valid @RequestBody SubmsissionRequestDTO submission) {
        Submission createdSubmission = submissionService.createSubmission(submission);
        Map<String, Object> response = new HashMap<>();
        response.put("submissionId", createdSubmission.getId());
        response.put("status", createdSubmission.getStatus());
        response.put("message", "Submission received and queued for execution.");

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<Submission>> getAllSubmissions(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) UUID problemId,
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
    public ResponseEntity<?> getSubmissionById(@PathVariable Long id) {
        Optional<Submission> maybe = submissionService.findById(id);

        if (maybe.isPresent()) {
            SubmissionResponseDTO dto = mapToDto(maybe.get());
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Submission not found", "submissionId", id));
        }
    }


    private SubmissionResponseDTO mapToDto(Submission s) {
        SubmissionResponseDTO dto = new SubmissionResponseDTO();
        dto.setId(s.getId());
        dto.setStatus(s.getStatus());
        dto.setStdout(s.getOutput());
        dto.setStderr(s.getErr());
        dto.setExecTimeMs(s.getExecTimeMs());
        dto.setCreatedAt(s.getSubmittedAt());
        dto.setFinishedAt(s.getFinishedAt());
        return dto;
    }

}