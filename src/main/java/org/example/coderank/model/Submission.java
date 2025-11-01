package org.example.coderank.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "submissions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Submission {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull(message = "Problem ID is required")
    @Column(name = "problem_id", nullable = false)
    private Long problemId;
    
    @NotNull(message = "User ID is required")
    @Column(name = "user_id", nullable = false)
    private Long userId;
    
    @NotBlank(message = "Code is required")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String code;
    
    @NotBlank(message = "Language is required")
    @Column(nullable = false)
    private String language;
    
    @Column(nullable = false)
    private String status;
    
    @Column(name = "execution_time")
    private Integer executionTime;
    
    @Column
    private String output;
    
    @Column(name = "submitted_at")
    private LocalDateTime submittedAt;
    
    @PrePersist
    protected void onCreate() {
        submittedAt = LocalDateTime.now();
    }
}
