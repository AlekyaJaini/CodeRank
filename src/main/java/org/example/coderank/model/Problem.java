package org.example.coderank.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "problems")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Problem {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private java.util.UUID id;


    @Column(unique = true, nullable = false)
    private Integer externalId; // API-facing ID
    
    @NotBlank(message = "Title is required")
    @Column(nullable = false)
    private String title;
    
    @NotBlank(message = "Description is required")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;
    
    @NotBlank(message = "Difficulty is required")
    @Column(nullable = false)
    private String level;
    
    @Column(name = "acceptance_rate")
    private Double acceptanceRate;
    
    @Column(columnDefinition = "TEXT")
    private String testCases;
    
    @Column(columnDefinition = "TEXT")
    @JsonProperty("constraints")
    private String constraints;

    @Column(columnDefinition = "time_limit_ms")
    private Integer time_limit_ms;

    @Column(columnDefinition = "memory_limit_kb")
    private Integer memory_limit_kb;
}
