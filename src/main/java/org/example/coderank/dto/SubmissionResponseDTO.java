package org.example.coderank.dto;

import lombok.Data;

import java.time.Instant;

// SubmissionResponseDTO.java
@Data
public class SubmissionResponseDTO {
    private Long id;
    private String status;
    private String stdout;
    private String stderr;
    private Long execTimeMs;
    private Instant createdAt;
    private Instant finishedAt;
}