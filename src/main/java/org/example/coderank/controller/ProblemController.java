package org.example.coderank.controller;

import org.example.coderank.model.Problem;
import org.example.coderank.service.ProblemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/problems")
public class ProblemController {
    
    private final ProblemService problemService;
    
    @Autowired
    public ProblemController(ProblemService problemService) {
        this.problemService = problemService;
    }
    
    @GetMapping
    public ResponseEntity<List<Problem>> getAllProblems(
            @RequestParam(required = false) String difficulty,
            @RequestParam(required = false) String search) {
        
        List<Problem> problems;
        
        if (difficulty != null) {
            problems = problemService.getProblemsByDifficulty(difficulty);
        } else if (search != null) {
            problems = problemService.searchProblemsByTitle(search);
        } else {
            problems = problemService.getAllProblems();
        }
        
        return ResponseEntity.ok(problems);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Problem> getProblemById(@PathVariable Long id) {
        return problemService.getProblemById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<Problem> createProblem(@RequestBody Problem problem) {
        Problem createdProblem = problemService.createProblem(problem);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProblem);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Problem> updateProblem(@PathVariable Long id, @RequestBody Problem problem) {
        try {
            Problem updatedProblem = problemService.updateProblem(id, problem);
            return ResponseEntity.ok(updatedProblem);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProblem(@PathVariable Long id) {
        problemService.deleteProblem(id);
        return ResponseEntity.noContent().build();
    }
}
