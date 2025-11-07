package org.example.coderank.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "submissions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Submission {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id", nullable = false)
    private Problem problem;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

//    @NotNull(message = "Problem ID is required")
//    @Column(name = "problem_id", nullable = false)
//    private UUID problemId;
    
//    @NotNull(message = "User ID is required")
//    @Column(name = "user_id", nullable = false)
//    private Long userId;
    
    @NotBlank(message = "Code is required")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String code;
    
    @NotBlank(message = "Language is required")
    @Column(nullable = false)
    private String language;
    
    @Column(nullable = false)
    private String status;
//
//    @Column(name = "execution_time")
//    private Integer executionTime;
//
    @Column
    private String output;

    @Column
    private String stdin;


    @Column
    private String err;

    @Column
    private Long execTimeMs;

    @CreationTimestamp
    @Column(name = "submitted_at", nullable = false, updatable = false)
    private Instant submittedAt;

    @Column(name = "finished_at")
    private Instant finishedAt;

    private Integer runtimeMs;  // execution time in ms
    private Integer memoryKb;   // memory used in KB
    
//    @PrePersist
//    protected void onCreate() {
//        submittedAt = LocalDateTime.now();
//    }
}
