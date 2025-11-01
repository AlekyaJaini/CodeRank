package org.example.coderank.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "problems")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Problem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Title is required")
    @Column(nullable = false)
    private String title;
    
    @NotBlank(message = "Description is required")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;
    
    @NotBlank(message = "Difficulty is required")
    @Column(nullable = false)
    private String difficulty;
    
    @Column(name = "acceptance_rate")
    private Double acceptanceRate;
    
    @Column(columnDefinition = "TEXT")
    private String testCases;
    
    @Column(columnDefinition = "TEXT")
    private String constraints;
}
